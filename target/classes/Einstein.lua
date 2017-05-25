
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


local c1=variable("c1")
local c2=variable("c2")
local c3=variable("c3")
local b1=variable("b1")
local b2=variable("b2")
local b3=variable("b3")

local constraintC=(((c1)'<'(integer(3)))"&&"((c2)'<'(integer(3)))"&&"((c3)'<'(integer(3))))
local constraintB=(((b1)'<'(integer(3)))"&&"((b2)'<'(integer(3)))"&&"((b3)'<'(integer(3))))

local jaune=(c1)'='(integer(0))
local cafe=(b3)'='(integer(2))
local vert=(c3)'='(integer(1))

local tempconstraint=(jaune)'&&'(cafe)'&&'(vert)
local tempconstraint=(tempconstraint)'&&'(((b1+b2)'='(integer(3)))'||'((b2+b3)'='(integer(3))))



local constraint=(constraintB)'&&'(constraintC)'&&'(tempconstraint)'&&'((c1)'!='(c2))'&&'((c1)'!='(c3))'&&'((c2)'!='(c3))'&&'((b1)'!='(b2))'&&'((b1)'!='(b3))'&&'((b2)'!='(b3))


local res=(constraint)'&&'((b1+b2+b3)'='(c1+c2+c3))
res:todot('testdot.dot')

print("ji endo")

