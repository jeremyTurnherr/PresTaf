local Presburger = {} -- the table representing the class, which will double as the metatable for the instances
Presburger.__index = Presburger -- failed table lookups on the instances should fallback to the class table, to get methods
Term = require("Term")
NPF = require("NPF")

function Presburger.new(expr, var, value)
    self.expr = expr
    self.var = var
    self.value = value
end

function Presburger.tostring(self)
	return self.expr
end

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

function and(self, b)
    newVar = Term.merge(self.var, b.var)
    u = self:addVariable(newVar)
    v = b:addVariable(newVar)
    return Presburger.new(self.expr .. " && " .. b.expr, newVar, u:and(v))
end

function or(self, b)
    newVar = Term.merge(self.var, b.var)
    u = self:addVariable(newVar)
    v = b:addVariable(newVar)
    return Presburger.new(self.expr .. " || " .. b.expr, newVar, u:or(v))
end

function imply(self, b)
    newVar = Term.merge(self.var, b.var)
    u = self:addVariable(newVar)
    v = b:addVariable(newVar)
    return Presburger.new(self.expr .. " -> " .. b.expr, newVar, u:imply(v))
end


function equiv(self, b)
    newVar = Term.merge(self.var, b.var)
    u = self:addVariable(newVar)
    v = b:addVariable(newVar)
    return Presburger.new(self.expr .. " <-> " .. b.expr, newVar, u:equiv(v))
end

function not(self)
   return Presburger.new("!" .. self.expr, self.var, self.value:not())
end

function table.copy(org, srcPos, desPos, dest, endPos)
    if desPos <= endPos and srcPos <= len(org) then
        dest[desPos] = org[srcPos]
        table.insert(t, table.copy(org, srcPos + 1, desPos + 1, dest, endPos)
    end
end

function exists(self, i, v)
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

function forall(self, i, v)
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

function equals(t1, t2)
    t = t2:minus(t1)
    return Presburger.new(t1:tostring() .. " = " .. t2:tostring(), 
        t.var, 
        NPF.equals(convert(t.coef), -t.constant, t.coef.length))
end

function notEquals(t1, t2)
    t = t2:minus(t1)
    return Presburger.new(t1:tostring() .. " != " .. t2:tostring(), 
        t.var, 
        NPF.notEquals(convert(t.coef), -t.constant, t.coef.length))
end

function less(t1, t2)
    t = t2:minus(t1)
    return Presburger.new(t1:tostring() .. " < " .. t2:tostring(), 
        t.var, 
        NPF.greater(convert(t.coef), -t.constant, t.coef.length))
end

function lessEquals(t1, t2)
    t = t2:minus(t1)
    return Presburger.new(t1:tostring() .. " <= " .. t2:tostring(), 
        t.var, 
        NPF.greaterEquals(convert(t.coef), -t.constant, t.coef.length))
end

function greater(t1, t2)
    t = t1:minus(t2)
    return Presburger.new(t1:tostring() .. " > " .. t2:tostring(), 
        t.var, 
        NPF.greater(convert(t.coef), -t.constant, t.coef.length))
end

function greaterEquals(t1, t2)
    t = t1:minus(t2)
    return Presburger.new(t1:tostring() .. " .= " .. t2:tostring(), 
        t.var, 
        NPF.greaterEquals(convert(t.coef), -t.constant, t.coef.length))
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
