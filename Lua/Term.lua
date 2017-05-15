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
	
	for i=1,(self:nbVar()) do
		print("-"..self.coef[i])
		negCoef[i]=-self.coef[i]
	end
	print("-----")
	return Term.new("-"..self.expr,self.var,negCoef,-self.constant)
end

function Term.plus(self,t) 
	plusvar=merge(self.var,t.var)
	pluscoef={}
	for i=1,len(plusvar) do
		pluscoef[i]=0
	end
	k=1
	i=1
	print("--1--")
	while i<=len(plusvar) and k<=self:nbVar() do
		if plusvar[i]==self.var[i] then
			pluscoef[i]=self.coef[i]
			k=k+1
			
		end
		i=i+1
	end
	print("--1--")
	
	while i<=len(plusvar) and k<=t:nbVar() do
		if plusvar[i]==t.var[i] then
			pluscoef[i]=pluscoef[i]+t.coef[i]
			k=k+1
			
		end
		i=i+1
	end
	print("--1--")
	return Term.new(self.expr.." + "..t.expr,plusvar,pluscoef,self.constant+t.constant)
	--~ return Term.new(" + ",plusvar,pluscoef,self.constant+t.constant)
	
end

function Term.minus(self,t) 
	plusvar=merge(self.var,t.var)
	pluscoef={}
	for i=1,len(plusvar) do
		pluscoef[i]=0
	end
	k=1
	i=1
	while i<=len(plusvar) and k<=self:nbVar() do
		if plusvar[i]==self.var[i] then
			pluscoef[i]=self.coef[i]
			k=k+1
			
		end
		i=i+1
	end
	
	while i<=len(plusvar) and k<=t:nbVar() do
		if plusvar[i]==t.var[i] then
			pluscoef[i]=pluscoef[i]-t.coef[i]
			k=k+1
			
		end
		i=i+1
	end
	return Term.new(self.expr.." + "..t.expr,plusvar,pluscoef,self.constant-t.constant)
	
end
	



function len(tab)
	Count = 0
	for k,i in pairs(tab) do
	  Count = Count + 1
	end
	return Count
end


function merge(a,b) -- deux listes de int
    lena=len(a)
    lenb=len(b)
    res={}
    n=1
    ai=1
    bi=1
    while ai<=lena and bi<=lenb do
		if a[ai] == b[bi] then
			res[n]=a[ai]
			ai=ai+1
			bi=bi+1
			n=n+1
		else if a[ai] <	b[bi] then
				res[n]=a[ai]
				ai=ai+1
				n=n+1
			else 
				res[n]=b[bi]
				bi=bi+1
				n=n+1
			end
		end
    end
    while ai<=lena do
		res[n]=a[ai]
		ai=ai+1
		n=n+1
	end
	while bi<=lenb do
		res[n]=b[bi]
		bi=bi+1
		n=n+1
	end
	return res
end

function getIndex(v)
    o = Term.map[v]
    if o ~= nil then
        return o
    else
        Term.map[v] = Term.size
        Term.size = Term.size + 1
        return Term.size - 1
    end
end

function factor(a, v)
    index = getIndex(v)
    return Term.new(a .. "*" .. v, {index}, {a}, 0)
end

function variable(v)
    index = getIndex(v)
    return Term.new(v, {index}, {1}, 0)
end

function integer(a)
    return Term.new(a, {}, {}, a)
end


Term.size=0



local module={}

Term.map = {}

module.Term=Term
module.merge=merge

module.getIndex = getIndex
module.map = map
module.factor = factor
module.variable = variable
module.integer = integer

return module
