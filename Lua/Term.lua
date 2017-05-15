local Term = {} -- the table representing the class, which will double as the metatable for the instances
Term.__index = Term -- failed table lookups on the instances should fallback to the class table, to get methods

-- syntax equivalent to "MyClass.new = function..."
function Term.new(expr,var,coef,constant)
  local self = setmetatable({}, Term)
  self.expr=expr
  self.var=var
  self.coef=coef
  self.constant=constant
  return self
end

function Term.tostring(self)
	return self.expr
end

function Term.debug(self)
	res=''
	for k,i in pairs(self.var) do
		if not( i ==0 )then
			res=res..self.coef[k].."* V"..self.var[k].." + "
			--~ res=res.."* V"..i.." + "
		end
	end
	return res.." "..tostring(self.constant)
end


function Term.nbVar(self)
	Count = 0
	for k,i in pairs(self.var) do
	  Count = Count + 1
	end
	return Count
end


function Term.neg(self)
	negCoef={}
	print("-----")
	for i=1,10 do
		negCoef[i]=-self.coef[i]
	end
	print("-----")
	return Term.new("-"..self.expr,self.var,negCoef,-self.constant)
	--~ return 0
end

function len(tab)
	Count = 0
	for k,i in pairs(tab) do
	  Count = Count + 1
	end
	return Count
end


function merge(a,b) -- deux listes de int
    n=0
    ai=0
    bi=0
    lena=len(a)
    lenb=len(b)
    while ai<lena and bi < lenb do
        if a[ai] == b[bi] then
            ai=ai+1
            bi=bi+1
            n=n+1
        else if a[ai]<b[bi] then
				ai=ai+1
				n=n+1
			else
				bi=bi+1
				n=n+1
			end
		end
    end
end


Term.size=0



local module={}

module.Term=Term
module.merge=merge

return module
