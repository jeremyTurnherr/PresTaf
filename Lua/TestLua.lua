
print("========run1========")

print(prestaf)

print(prestaf.Wowie)

prestaf:Wowie()

prestaf:Wowie_parameter(1)
prestaf:Wowie_inc(1)

print("-")

function run(pp)

  print("========run========")

  print(pp)

  print(pp.Wowie)

  pp:Wowie()
  pp:Wowie_parameter(1)
  pp:Wowie_inc(1)
  pp:Wowie_inc(1)

end

local co=coroutine.create(run)

--~ print(coroutine.resume(co))

run(prestaf)
