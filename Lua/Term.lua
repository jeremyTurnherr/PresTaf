--~ presburger=require('presburger')

local Term = {} 
Term.__index = Term 


--~ Constructeur de terme
--~ (expression: string, var: array, coef: array, constant: integer)
--~ 
--~ (ce constructeur ne doit pas etre utilisé directement)
function Term.new(expr,var,coef,constant)
  local self = setmetatable({}, Term)
  self.expr=expr
  self.var=var
  self.coef=coef
  self.constant=constant
  return self
end


--~ permet d'afficher l'expression
function Term.tostring(self)
	return self.expr
end

--~ affiche le debuggage
function Term.debug(self)
	res=''
	for k,i in pairs(self.var) do
		if not( i ==0 )then
			res=res..self.coef[k].."* V"..self.var[k].." + "
		end
	end
	return res.." "..tostring(self.constant)
end

--~ retourne le nombre de valeurs dans var
function Term.nbVar(self)
	Count = 0
	for k,i in pairs(self.var) do
	  Count = Count + 1
	end
	return Count
end

--~ retourne la negation du terme
function Term.neg(self)
	negCoef={}
	
	for i=1,(self:nbVar()) do
		negCoef[i]=-self.coef[i]
	end
	return Term.new("-"..self.expr,self.var,negCoef,-self.constant)
end

function Term.__min(self)
	return self:neg()
end

--~ <<surchargé>> l'addition de deux termes
function Term.plus(self,t) 
	plusvar=merge(self.var,t.var)
	pluscoef={}
	for i=1,len(plusvar) do
		pluscoef[i]=0
	end
	local k
	local i
	k=1
	i=1
	while i<=len(plusvar) and k<=self:nbVar() do
		if plusvar[i]==self.var[k] then
			pluscoef[i]=self.coef[k]
			k=k+1
			
		end
		i=i+1
	end
	
	k=1
	i=1
	
	while i<=len(plusvar) and k<=t:nbVar() do
		if plusvar[i]==t.var[k] then
			pluscoef[i]=pluscoef[i]+t.coef[k]
			k=k+1
			
		end
		i=i+1
	end
	return Term.new(self.expr.." + "..t.expr,plusvar,pluscoef,self.constant+t.constant)
	
end

function Term.__add(t1,t2)
	return t1:plus(t2)
end

--~ <<surchargé>> soustraction de deux termes
function Term.minus(self,t) 
	plusvar=merge(self.var,t.var)
	pluscoef={}
	local k
	local i
	for i=1,len(plusvar) do
		pluscoef[i]=0
	end
	k=1
	i=1
	while i<=len(plusvar) and k<=self:nbVar() do
		if plusvar[i]==self.var[k] then
			pluscoef[i]=self.coef[k]
			k=k+1
			
		end
		i=i+1
	end
	
	k=1
	i=1
	
	while i<=len(plusvar) and k<=t:nbVar() do
		if plusvar[i]==t.var[k] then
			pluscoef[i]=pluscoef[i]-t.coef[k]
			k=k+1
			
		end
		i=i+1
	end
	return Term.new(self.expr.." - "..t.expr,plusvar,pluscoef,self.constant-t.constant)
	
end

function Term.__sub(t1,t2)
	return t1:minus(t2)
end
	



function len(tab)
	Count = 0
	for k,i in pairs(tab) do
	  Count = Count + 1
	end
	return Count
end

--~ fusionne les vars de deux termes
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

--~ recupere le terme dans la table si il existe, sinon le crée
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

--~ <<surchargé>> multiplication entre un entier et un autre terme
function factor(a, v)
	if (type(a)~="number" and type(v)~="number") then print("Warning: there should be a number for operator *") end
	if type(a)=="number" then 
		index = getIndex(v.expr)
		return Term.new(a .. "*" .. v.expr, {index}, {a}, 0)
	else
		index = getIndex(a.expr)
		return Term.new(v .. "*" .. a.expr, {index}, {v}, 0)
	end
    
end

function Term.__mul(int,t)
	return factor(int,t)
end

--~ à utiliser pour initialiser un terme avec une variable
function variable(v)
    index = getIndex(v)
    --~ print("index: "..index)
    return Term.new(v, {index}, {1}, 0)
end


--~ à utiliser pour initialiser un terme avec une constante entière
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
