local Presburger = {} -- the table representing the class, which will double as the metatable for the instances
Presburger.__index = Presburger -- failed table lookups on the instances should fallback to the class table, to get methods

function Presburger.new(expr, var, value)
    self.expr = expr
    self.var = var
    self.value = value
end

function Presburger.tostring(self)
	return self.expr
end

function statistics(self)
    if value == nil then
        return "..... null"
    else
        --if value
    end
end

local module = {}

module.Presbuger = Presburger
