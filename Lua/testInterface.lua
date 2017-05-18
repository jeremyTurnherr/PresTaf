
term=require('Term')
print(prestaf)
preburger=require('presburger')


function printab(tab)
	res="{"
	for k,i in pairs(tab) do
		res=res..k..":   "..i.."  ,"
	end
	print(res.."}")
end



print("-")
function run(pres)
	--~ local i=term.Term.new("exp",{1,2,3},{2,2,2},5)
	--~ print("-")
	--~ 
	--~ print(i:nbVar())
	--~ print(i.var[2])
	--~ print("-")
	--~ print(i:debug())
	--~ print("-merge-")
	--~ printab(term.merge({1,2,3},{3,2,1}))
	--~ print("-")
	--~ print(term.Term.size)
	--~ print("-neg-")
	--~ local y=i:neg()
	--~ print(y:debug())
	--~ print("-plus-")
	--~ local z=y:plus(i)
	--~ print(z:debug())
	--~ print("-minus-")
	--~ print(z:minus(i):debug())
	--~ print("-factor 9-")
	--~ print(term.factor(9,"expppp"):debug())
	--~ print("-variable-")
	--~ print(term.variable("expppp"):debug())
	--~ print("-integer-")
	--~ print(term.integer(42):debug())
	--~ print("fainaru-")
	--~ local x=variable('x')
	--~ local y=variable('y')
	
	--~ print((2*x+y):tostring())
	
	print("-----presburger, hajime!!!-----")
	--~ local t2=variable("u")
	--~ local t=
	
	--~ print(prestaf:what())
	--~ local p=equals(3*x,y)
	--~ print(p)
	--~ print(p:tostring())
	--~ print(p.value:toDot())

	--~ print(equals((2*x)-y,7*variable('z')).value:toDot())
	--~ print(equals(variable("a"),integer(0)).value:toDot())
	local t=variable("y")
	local pif=variable("u")
	
	
	--~ print(equals(pif,t+integer(1)).value:toDot())
	equals(pif,t+integer(1)):todot("testsave.txt")
	--~ print((pif==integer(3)).value:toDot())
	
	print("ji endo")

end

local co=coroutine.create(run)


run(prestaf)

