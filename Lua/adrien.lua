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
    index = get
end

Term.map = {}

module.getIndex = getIndex
module.map = map
