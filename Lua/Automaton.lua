

function len(tab)
	local Count = 0
	for k,i in pairs(tab) do
	  Count = Count + 1
	end
	return Count
end

function fillsuccparam(succ)

	prestaf:init_succ(len(succ),len(succ[1]))
	for postate,state in pairs(succ) do
		for posletter,t in pairs(state) do
			prestaf:fillsucc(postate,posletter,t)
		end
	end

end

function fillfinalparam(b)

	prestaf:init_final(len(b))
	for pos,val in pairs(b) do
	  prestaf:fillfinal(pos,val)
	end

end

function createAutomaton(initial,succ,finals)
	fillsuccparam(succ)
	fillfinalparam(finals)
	return prestaf:createAutomaton(initial)
	
end
	




local module={}


module.createAutomaton=createAutomaton

return module
