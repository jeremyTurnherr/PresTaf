
term=require('Term')
preburger=require('presburger')


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
	
local t=variable("y")
local pif=variable("u")

--~ local res=equals(t,integer(1)):E(t)
local res=(t)'!='(integer(1))
--~ local n=res:A(t)
res:todot("testdot.txt")
--~ local hmmm=(pif==integer(1))
--~ result():todot("testdot.txt")
--~ :todot("testsave.txt")

print("ji endo")

