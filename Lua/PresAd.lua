local Presburger = {} -- the table representing the class, which will double as the metatable for the instances
Presburger.__index = Presburger -- failed table lookups on the instances should fallback to the class table, to get methods
term = require("Term")
-- NPF = require("NPF")

--[[
    Instantiate a new Presburger formula such as : 2x - y = 3
    in this case we will we create something like : Presburger.new("2x - y", {x, y}, 3)
]]
function Presburger.new(expr, var, value)
    self.expr = expr
    self.var = var
    self.value = value
end

function Presburger.tostring(self)
	return self.expr
end

--[[
    statistics provides informations on the current formula.
    For instance, we can know the deapth of the automata, if it is always true or false, etc.
]]
function statistics(self)
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

function bracket(self)
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
function addVariable(self, v)
    lenvar = len(self.var)
    lenv = len(v)
    if lenvar == len then
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
function And(self, b)
    print("I am here")
    newVar = term.Term.merge(self.var, b.var)
    u = self:addVariable(newVar)
    v = b:addVariable(newVar)
    return Presburger.new(self.expr .. " && " .. b.expr, newVar, u:and(v))
end

--[[
    Or gives the capacity to specify more constraint on a formula. Like : 2x - y = 3 || x == 0
]]
function Or(self, b)
    newVar = term.Term.merge(self.var, b.var)
    u = self:addVariable(newVar)
    v = b:addVariable(newVar)
    return Presburger.new(self.expr .. " || " .. b.expr, newVar, u:or(v))
end


--[[
    imply gives the capacity to specify more constraint on a formula. 
    Like : 2x - y = 3 -> x > 2 && y > 0
]]
function imply(self, b)
    newVar = term.Term.merge(self.var, b.var)
    u = self:addVariable(newVar)
    v = b:addVariable(newVar)
    return Presburger.new(self.expr .. " -> " .. b.expr, newVar, u:imply(v))
end

--[[
    equiv gives the capacity to specify more constraint on a formula. 
    Like : 2x - y = 3 <-> x == y && x = 3
]]
function equiv(self, b)
    newVar = term.Term.merge(self.var, b.var)
    u = self:addVariable(newVar)
    v = b:addVariable(newVar)
    return Presburger.new(self.expr .. " <-> " .. b.expr, newVar, u:equiv(v))
end


--[[
    Not gives the capacity to specify more constraint on a formula. 
    Like : 2x - y = 3 -> !(x = 0 || y = 0)
]]
function Not(self)
   return Presburger.new("!" .. self.expr, self.var, self.value:not())
end

--[[
    Rewrite of java function : System.arraycopy
]]
function table.copy(org, srcPos, desPos, dest, endPos)
    if desPos <= endPos and srcPos <= len(org) then
        dest[desPos] = org[srcPos]
        table.insert(t, table.copy(org, srcPos + 1, desPos + 1, dest, endPos)
    end
end

--[[
    Check if the variable v exists
]]
function E(self, i, v)
    pos = table.binsearch(self.var, i)
    if pos < 0 or pos >= len(self.var) then
        return Presburger.new("A." .. v .. " " .. self.expr, self.var, self.value)
    else
	newVar = {}
        table.copy(self.var, 0, 0, newVar, pos)
        table.copy(self.var, pos + 1, pos, newVar, len(self.var) - 1 - pos)
        return Presburger.new("E." .. v .. " " .. expr, newVar, value:exists(pos))
    end
end

--[[
    Check if all values are v
]]
function A(self, i, v)
    pos = table.binsearch(self.var, i)
    if pos < 0 or pos >= len(self.var) then
        return Presburger.new("A." .. v .. " " .. self.expr, self.var, self.value)
    else
	newVar = {}
        table.copy(self.var, 0, 0, newVar, pos)
        table.copy(self.var, pos + 1, pos, newVar, len(self.var) - 1 - pos)
        return Presburger.new("A." .. v .. " " .. expr, newVar, value:forall(pos))
    end
end

function convert(coef)
    res = {}
    for k, i in pairs(coef) do
        res[2 * k + 1] = i
        res[2 * k] = k
    end
    return res
end

--[[
    Check if t1 == t2
]]
function equals(t1, t2)
    t = t2:minus(t1)
    return Presburger.new(t1:tostring() .. " = " .. t2:tostring(), 
        t.var, 
        -- NPF.equals(convert(t.coef), -t.constant, t.coef.length))
end

--[[
    Check if t1 != t2
]]
function notEquals(t1, t2)
    t = t2:minus(t1)
    return Presburger.new(t1:tostring() .. " != " .. t2:tostring(), 
        t.var, 
        -- NPF.notEquals(convert(t.coef), -t.constant, t.coef.length))
end

--[[
    Check if t1 < t2
]]
function less(t1, t2)
    t = t2:minus(t1)
    return Presburger.new(t1:tostring() .. " < " .. t2:tostring(), 
        t.var, 
        NPF.greater(convert(t.coef), -t.constant, t.coef.length))
end

--[[
    Check if t1 <= t2
]]
function lessEquals(t1, t2)
    t = t2:minus(t1)
    return Presburger.new(t1:tostring() .. " <= " .. t2:tostring(), 
        t.var, 
        -- NPF.greaterEquals(convert(t.coef), -t.constant, t.coef.length))
end

--[[
    Check if t1 > t2
]]
function greater(t1, t2)
    t = t1:minus(t2)
    return Presburger.new(t1:tostring() .. " > " .. t2:tostring(), 
        t.var, 
        -- NPF.greater(convert(t.coef), -t.constant, t.coef.length))
end

--[[
    Check if t1 >= t2
]]
function greaterEquals(t1, t2)
    t = t1:minus(t2)
    return Presburger.new(t1:tostring() .. " .= " .. t2:tostring(), 
        t.var, 
        -- NPF.greaterEquals(convert(t.coef), -t.constant, t.coef.length))
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
