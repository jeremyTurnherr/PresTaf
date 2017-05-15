function getIndex(v)
    o = Term.map[v]
    if o ~= nil then
        return o
    else
        Term.map[v] = Term.size
        Term.size = Term.size + 1
        return Term.size - 1
    end
end

function factor(a, v)
    index = getIndex(v)
    return Term.new(a .. "*" .. v, {index}, {a}, 0)
end

function variable(v)
    index = getIndex(v)
    return Term.new(v, {index}, {1}, 0)
end

function integer(a)
    return Term.new(a, {}, {}, a)
end

Term.map = {}

module.getIndex = getIndex
module.map = map
module.factor = factor
module.variable = variable
module.integer = integer
