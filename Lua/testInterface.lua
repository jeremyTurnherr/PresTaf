
term=require('Term')
print(prestaf)


function printab(tab)
	res="{"
	for k,i in pairs(tab) do
		res=res..k..":   "..i.."  ,"
	end
	print(res.."}")
end



print("-")
function run(pres)
	local i=term.Term.new("exp",{1,2,3},{2,2,2},5)
	print("-")
	
	print(i:nbVar())
	print(i.var[2])
	print("-")
	print(i:debug())
	print("-merge-")
	printab(term.merge({1,2,3},{3,2,1}))
	print("-")
	print(term.Term.size)
	print("-neg-")
	local y=i:neg()
	print(y:debug())
	print("-plus-")
	local z=y:plus(i)
	print(z:debug())
	print("-minus-")
	print(z:minus(i):debug())
	print("-factor 9-")
	print(term.factor(9,"expppp"):debug())
	print("-variable-")
	print(term.variable("expppp"):debug())
	print("-integer-")
	print(term.integer(42):debug())

	
	
	print("ji endo")

end

local co=coroutine.create(run)


run(prestaf)

