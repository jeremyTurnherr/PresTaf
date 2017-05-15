
term=require('Term')
print(prestaf)



print("-")
function run(pres)
	local i=term.Term.new("exp",{1,2,3},{2,2,2},5)
	print("-")
	print(i:nbVar())
	print(i.var[2])
	print("-")
	print(i:debug())
	print("-")
	print(term.merge({1,2,3},{3,2,1}))
	print("-")
	print(term.Term.size)
	print("--")
	print(i.neg())
	print("-")
	
	
	print("ji endo")

end

local co=coroutine.create(run)


run(prestaf)

