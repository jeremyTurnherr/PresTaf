
term=require('Term')
preburger=require('presburger')
automaton=require('Automaton')


function printab(tab)
	res="{"
	for k,i in pairs(tab) do
		res=res..k..": "..i.."  ,"
	end
	print(res.."}")
end



function run(pres)
	
	
	print("-----presburger, hajime!!!2-----")
	
	local t=variable("y")
	local pif=variable("u")
	
	
	local hmmm=(pif==integer(1))
	print(result().value:toDot())
	--~ :todot("testsave.txt")
	
	print("ji endo")

end

--~ local co=coroutine.create(run)


--~ run(prestaf)

print("-----presburger, hajime!!!-----")
	
--~ local t=variable("y")
--~ local pif=variable("u")

--~ local res=((t)'='(integer(2)))'||'((t)'='(integer(3)))
--~ res:todot("testdot.txt")
local w=variable('w')
local x=variable('x')
local y=variable('y')
local z=variable('z')

--~ local nope=createAutomaton(0,{{0,1,2},{1,2,0},{2,0,1}},{false,false,false})


--~ local res=_E(r,_A(x,_A(y,_E(z,(y+(2*x)-(3*z)+w-q)'='(17*r)))))
local res=((((3*w)+x)-y)'='(z))
res:todot('testdot.txt')

print("ji endo")

