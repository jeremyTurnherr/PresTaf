local Presburger = {} -- the table representing the class, which will double as the metatable for the instances
Presburger.__index = Presburger -- failed table lookups on the instances should fallback to the class table, to get methods
term = require("Term")




function filltabparam(t)

	prestaf:init_tab(len(t))
	for pos,val in pairs(t) do
	  prestaf:fill(pos,val)
	end

end

function printab(tab)
	res="{"
	for k,i in pairs(tab) do
		res=res..k..":   "..i.."  ,"
	end
	print(res.."}")
end


--[[
    Instantiate a new Presburger formula such as : 2x - y = 3
    in this case we will we create something like : Presburger.new("2x - y", {x, y}, 3)
]]
function Presburger.new(expr, var, value)
	local self = setmetatable({}, Presburger)
    self.expr = expr
    self.var = var
    self.value = value
    return self
end

function Presburger.tostring(self)
	return self.expr
end

--[[
    statistics provides informations on the current formula.
    For instance, we can know the deapth of the automata, if it is always true or false, etc.
]]
function Presburger.statistics(self)
    if self.value == nil then
        return "..... null"
    else
        if self.value:isZero() then
            return "zero"
        else
            if self.value:isOne() then
                return "one"
            else
                return "Size = " .. self:getNbStates() 
                  .. ", #SCC = " .. self:getNbSharedAutomata() 
                  .. ", #OutputAutomaton = " .. self:getNbOutputAutomata()
                  .. ", Deapth = " .. (value:deapth() + 1)
            end
        end
    end
end

function Presburger.bracket(self)
    return Presburger.new("(" .. self.expr .. ")", self.var, self.value)
end

function len(tab)
	Count = 0
	for k,i in pairs(tab) do
	  Count = Count + 1
	end
	return Count
end

--[[
    addVariable enables the user to add variables to an already existing formula
    For example, 2x - y = 3, if the user wants to add a variable 'z' he will use the method
]]
function Presburger.addVariable(self, v)
    lenvar = len(self.var)
    lenv = len(v)
    if lenvar == lenv then
        return self.value
    else
        b = {}
        for i = 1, i <= lenv do
            b[i] = true
        end
        k = 0
        for i = 1, i <= lenv and k <= lenvar do
            if v[i] == var[k] then
                b[i] = false
                k = k + 1
            end
        end
        return self.value:addVariable(b)
    end
end

--[[
    And gives the capacity to specify more constraint on a formula. Like : 2x - y = 3 && y < 2
]]
function Presburger.And(self, b)
    newVar = term.merge(self.var, b.var)
    u = self:addVariable(newVar)
    v = b:addVariable(newVar)
    return Presburger.new(self.expr .. " && " .. b.expr, newVar, u:And(v))
end

--[[
    Or gives the capacity to specify more constraint on a formula. Like : 2x - y = 3 || x == 0
]]
function Presburger.Or(self, b)
    newVar = term.merge(self.var, b.var)
    u = self:addVariable(newVar)
    v = b:addVariable(newVar)
    return Presburger.new(self.expr .. " || " .. b.expr, newVar, u:Or(v))
end


--[[
    imply gives the capacity to specify more constraint on a formula. 
    Like : 2x - y = 3 -> x > 2 && y > 0
]]
function Presburger.imply(self, b)
    newVar = term.merge(self.var, b.var)
    u = self:addVariable(newVar)
    v = b:addVariable(newVar)
    return Presburger.new(self.expr .. " -> " .. b.expr, newVar, u:imply(v))
end

--[[
    equiv gives the capacity to specify more constraint on a formula. 
    Like : 2x - y = 3 <-> x == y && x = 3
]]
function Presburger.equiv(self, b)
    newVar = term.merge(self.var, b.var)
    u = self:addVariable(newVar)
    v = b:addVariable(newVar)
    return Presburger.new(self.expr .. " <-> " .. b.expr, newVar, u:equiv(v))
end


--[[
    Not gives the capacity to specify more constraint on a formula. 
    Like : 2x - y = 3 -> !(x = 0 || y = 0)
]]
function Presburger.Not(self)
   return Presburger.new("!" .. self.expr, self.var, self.value:Not())
end

--[[
    Rewrite of java function : System.arraycopy
]]
function table.copy(org, srcPos, desPos, dest, endPos)
    if desPos <= endPos and srcPos <= len(org) then
        dest[desPos] = org[srcPos]
        table.insert(t, table.copy(org, srcPos + 1, desPos + 1, dest, endPos))
    end
end

--[[
    Rewrite of java function : Array.BinarySearch
]]
function table.binsearch(tab,i)
	local mini=1
	local maxi=len(tab)
	if (tab[1]==i) then
		return 1
	end
	while (mini<maxi) do
 
    	local moitie=math.floor((mini+maxi)/2)
    	if tab[moitie]==i then
        	return moitie
     	else if tab[moitie]<i then
				mini=moitie
			else
				maxi=moitie-1
			end
   
		end

	end
	--~ return 1
	return -1
end


--[[
    Check if the variable v exists
]]
function Presburger.E(self,var)
	print("wowieeeee")
	printab(self.var)
	print(getIndex(var.expr))
	print("wowieeeee")
    pos = table.binsearch(self.var, getIndex(var.expr))
    print("wowieeeee")
    if pos-1 < 0 or pos-1 >= len(self.var) then
		print(pos.."  ---")
        return Presburger.new("E." .. var.expr .. " " .. self.expr, self.var, self.value)
    else
		print("wowieeeee")
		newVar = {}
		print("wowieeeee")
        table.copy(self.var, 0, 0, newVar, pos)
        print("wowieeeee")
        table.copy(self.var, pos + 1, pos, newVar, len(self.var) - 1 - pos)
        print("wowieeeee")
        return Presburger.new("E." .. v .. " " .. expr, newVar, value:exists(pos))
    end
end

--[[
    Check if all values are v
]]
function Presburger.A(self, var)
	print("yyyyyyyac")
    pos = table.binsearch(self.var, getIndex(var.expr))
    print("yyyyyyyac")
    if pos < 0 or pos >= len(self.var) then
        return Presburger.new("A." .. var.expr .. " " .. self.expr, self.var, self.value)
    else
		newVar = {}
        table.copy(self.var, 0, 0, newVar, pos)
        table.copy(self.var, pos + 1, pos, newVar, len(self.var) - 1 - pos)
        return Presburger.new("A." .. var.expr .. " " .. expr, newVar, value:forall(pos))
    end
end

function convert(coef)
    res = {}
    local k
    local i
    for k, i in pairs(coef) do
        res[2 * k ] = i
        res[2 * k-1] = k-1
    end
    return res
end

--[[
    Check if t1 == t2
]]
function equals(t1, t2)
	print("-----coef---------")
	printab(t1.coef)
	printab(t2.coef)
    local t = t2:minus(t1)
    printab(t.coef)
    print(t:tostring())
    print("----convert------")
    filltabparam(convert(t.coef))
    print("--------------------")
    npf=prestaf:equals( -t.constant,len(t.coef))
   
    return Presburger.new(t1:tostring() .. " = " .. t2:tostring(),t.var,npf)--prestaf:equals( -t.constant,len(t.coef))
end

--~ term.Term.setmetatable(0, {  
	--~ __call = function(a, op)
				--~ if op == '=' then 
					--~ return function(b) 
							--~ return equals(a,b) 
						--~ end
				--~ else print("nope")
				--~ end
			--~ end
--~ })









--[[
    Check if t1 != t2
]]
function notEquals(t1, t2)
    local t = t2:minus(t1)
    filltabparam(convert(t.coef))
    return Presburger.new(t1:tostring() .. " != " .. t2:tostring(), 
        t.var, 
		prestaf:notEquals( -t.constant, len(t.coef)))
end

--[[
    Check if t1 < t2
]]
function less(t1, t2)
    t = t2:minus(t1)
    filltabparam(convert(t.coef))
    return Presburger.new(t1:tostring() .. " < " .. t2:tostring(), 
        t.var, 
        prestaf:greater( -t.constant, len(t.coef)))
end

--[[
    Check if t1 <= t2
]]
function lessEquals(t1, t2)
    t = t2:minus(t1)
    filltabparam(convert(t.coef))
    return Presburger.new(t1:tostring() .. " <= " .. t2:tostring(), 
        t.var, 
        prestaf:greaterEquals( -t.constant, len(t.coef)))
end

--[[
    Check if t1 > t2
]]
function greater(t1, t2)
    t = t1:minus(t2)
    filltabparam(convert(t.coef))
    temp=prestaf:greater( -t.constant, len(t.coef))
    return Presburger.new(t1:tostring() .. " > " .. t2:tostring(),t.var,temp)
end

--[[
    Check if t1 >= t2
]]
function greaterEquals(t1, t2)
    t = t1:minus(t2)
    filltabparam(convert(t.coef))
    return Presburger.new(t1:tostring() .. " .= " .. t2:tostring(), 
        t.var, 
        prestaf:greaterEquals(-t.constant, len(t.coef)))
end

function getNbStates(self)
    return sefl.value:getNbStates()
end

function getNbSharedAutomata(self)
    return sefl.value:getNbSharedAutomata()
end

function getNbOutputAutomata(self)
    return sefl.value:getNbOutputAutomata()
end

function Presburger.todot(self,file)
	local f = io.open(file, "w")
	io.output(f)
	io.write(self.value:toDot())
    io.close(f)   
end

function result(self)
	return Presburger.Result
end

Presburger.Result=nil





--[[
    Terms operators override
]]

term.Term.__call = function(t1, op)
				if op == '=' then 
					print("tente ça")
					return function(t2) 
							print("tente ça")
							return equals(t1,t2) 
						end
				
				end
				if op == '!=' then 
					return function(t2) 
							return notEquals(t1,t2) 
						end
				
				end
				if op == '<' then 
					return function(t2) 
							return less(t1,t2) 
						end
				
				end
				if op == '<=' then 
					return function(t2) 
							return lessEquals(t1,t2) 
						end
				
				end
				if op == '>' then 
					return function(t2) 
							return greater(t1,t2) 
						end
				
				end
				if op == '>=' then 
					return function(t2) 
							return greaterEquals(t1,t2) 
						end
				
				end
				
				
			end

--[[
    Presburger operators override
]]

Presburger.__call = function(p, op)
				if op == 'A' then 
					return function(term) 
							return p:A(term) 
						end
				
				end
				if op == 'E' then 
					return function(term) 
							return p:E(term) 
						end
				
				end
				if op == '!' then 
					return p:Not()
				
				end
				
				
				
			end



local module = {}

module.Presbuger = Presburger
module.convert = convert
module.equals = equals
module.notEquals = notEquals
module.less = less
module.lessEquals = lessEquals
module.greater = greater
module.greaterEquals = greaterEquals
module.getNbStates = getNbStates
module.getNbSharedAutomata = getNbSharedAutomata
module.getNbOutputAutomata = getNbOutputAutomata
module.result=result


return module