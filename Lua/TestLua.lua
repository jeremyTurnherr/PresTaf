
print("========run1========")

print(people)

print(people.Wowie)

people:Wowie()

people:Wowie_parameter(1)

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

run(people)
