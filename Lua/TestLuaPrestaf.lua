

print(prestaf)

print(prestaf.execPrestaf)

--~ prestaf:execPrestaf(["","test.mso"])

print("-")
function run(pres)

	pres:execPrestaf("test.mso")

end

local co=coroutine.create(run)


run(prestaf)
