

class Dt:
	"""contient une liste doublement chainee"""
	def __init__(self,l1):
		self.ls=set([l1])


class ListeDoubleChainee:
	"""liste d'etats de Qi qui lorsqu'on applique 'l' arrive dans Qj"""
	class Maillon:
		def __init__(self,prec,suiv,tete,q):
			self.prec=prec
			self.suiv=suiv
			self.tete=tete
			self.q=q
		
		def addEnd(self,maillon):
			if self.suiv==None:
				maillon.prec=self
				self.suiv=maillon
			else:
				self.suiv.addEnd(maillon)
		
		def addBeginning(self,maillon):
			self.prec=maillon
			maillon.suiv=self
				
		def __repr__(self):
			res="--("+str(self.q)+")"
			if self.suiv:
				res+=str(self.suiv)
			
			return res
		
	def __init__(self,i,symb,j):
		self.taille=0#S(i,a,j)
		self.deb=None
		self.symb=symb
		self.i=i
		self.j=j
		
	def add(self,etat):
		temp=self.Maillon(None,None,self,etat)
		if self.taille==0:
			self.deb=temp
		else:
			self.deb.addBeginning(temp)
			self.deb=temp
		self.taille+=1
		return temp
	
	def __repr__(self):
		res="{i "+str(self.i)+",a "+str(self.symb)+",j "+str(self.j)+"} "
		
		return res+str(self.deb)
			
		
	@staticmethod
	def generateDeb(listeclasses,alphabet,transitions):
		"""premiere generation des L(i,a,j)"""
		res=[]
		for i in range(1,len(listeclasses)):
			for j in range(len(listeclasses)):
				for symb in alphabet:
					temp=ListeDoubleChainee(i,symb,j)
					for t in transitions:
						if t[0]==symb and t[1] in listeclasses[i] and t[2] in listeclasses[j]:
							temp.add(t[2])
					res.append(temp)
		return res
			
		
		
class Delta:
	"""prend un etat p et une lettre et pointe vers le suivant"""
	def __init__(self,nblettres,nbetats):
		self.nblettres=nblettres
		self.nbetats=nbetats
		self.mat=[[None for n in range(nblettres)] for n2 in range(nbetats)]
		
	def __repr__(self):
		res=""
		for i in self.mat:
			res+="|"
			for y in i:
				if y:
					res+=str(y.q)
				else:
					res+=str(y)
				res+="|"
			res+="\n-------------------\n"
			
		return res
		
	@staticmethod
	def generateWithEtatAndLchaine(automata):
		res=Delta(len(automata.A),len(automata.Q))
		for t in automata.T:
			res.mat[t[1]][t[0]]=t[2]
		return res
		
		
	
class DeltaMoinsUn:
	"""prend lettre et q et retourne un pointeur sur l'ensemble des etats precedents"""
	def __init__(self,nbetats,nblettres):
		self.nblettres=nblettres
		self.nbetats=nbetats
		self.mat=[[[] for n in range(nbetats)] for n2 in range(nblettres)]
		
	def __repr__(self):
		res=""
		for i in self.mat:
			res+="|"
			for y in i:
				#~ if y:
					#~ res+=str(y.q)
				#~ else:
				res+=str(y)
				res+="|"
			res+="\n-------------------\n"
			
		return res
		
	@staticmethod
	def generateWithEtat(automata):
		res=Delta(len(automata.Q),len(automata.A))
		for t in automata.T:
			res.mat[t[0]][t[1]]
		return res
		
class DeltaPrime:
	"""liste des classes d'etats"""
	class DeltaMaillon:
		def __init__(self,prec,suiv,lchaine):
			self.prec=prec
			self.suiv=suiv
			self.listeChainee=lchaine
			
	def __init__(self,K):
		self.Kself=None#représente sa pos dans K
		self.l=None
		K.addDeltaPrime(self)
		
	def addListe(self,lchaine):
		self.l=lchaine
		
	def __repr__(self):
		return "-> "+str(self.l)
	
		
class Ksetashdey:
	"""l'ensemble des delta prime de longueur >=2"""
	def __init__(self):
		self.K=[]
		
	def addDeltaPrime(self,dprime):
		dprime.Kself=len(self.K)
		self.K.append(dprime)
		
	def removeDeltaPrime(self,id):
		self.K[id]=None
		
	def __repr__(self):
		res="------"
		for dp in self.K:
			if dp:
				res+="\n"+str(dp)
				res+="\n------"
		return res
	
	@staticmethod
	def generateAll(automata):
		classes=[set(),automata.F.copy(),automata.Q-automata.F]
		resK=Ksetashdey()
		resDelta=Delta(len(automata.A),len(automata.Q))
		resDeltaMoinsUn=DeltaMoinsUn(len(automata.Q),len(automata.A))
		reslchaine=[[[ListeDoubleChainee(x,y,z) for x in range(3)] for y in automata.A] for z in range(3)]
		
		for t in automata.T:
			t1f=1
			t2f=1
			if automata.TabFinal[t[1]]:
				t1f=2
			if automata.TabFinal[t[2]]:
				t2f=2
			
			resDelta.mat[t[1]][t[0]]=reslchaine[t2f][t[0]][t1f].add(t[1])
			resDeltaMoinsUn.mat[t[0]][t[2]].append(t[1])
		print("------------------------------------")
		print(resDelta)
		print("------------------------------------")
		for i in reslchaine:
			for y in i:
				for z in y:
					if z.deb:
						print(z)
		print("------------------------------------")
		print(resDeltaMoinsUn)

		#~ ll=ListeDoubleChainee.generateDeb([set(),automata.F.copy(),automata.Q-automata.F],automata.A,automata.T)
		for l in reslchaine:
			for y in l:
				for z in y:
					
					dp=DeltaPrime(resK)
					dp.addListe(z)
		input()
		return resK,resDelta,reslchaine,resDeltaMoinsUn
		


def not_exists_transitions_between_classes(classes,transitions,alphabet,t):
	"""fonction qui teste si il n'y a pas de transitions entre une classe et une liste de classe"""

	for i in range(1,t):
		classe=classes[i]
		for autre,j in [(classes[j],j) for j in range(t)]:
			
			
			for symb in alphabet:
				res=True
				for transition in transitions:
					if (transition[0]==symb):
						if (transition[1] in classe):
							res=res and transition[2] not in autre#or (transition[1] in autre and transition[2] in classe):#si il existe une transition d'une classe vers une autre
				if res:
					return i,j,symb
		

	return None
	
def find_classes_indices(Q,transitions,t):
	"""trouve les indices i, j1 et j2 du texte, pas trouvé de noms pour cette fonction"""
	for i in range(len(Q)):
		for j1 in range(t):
			for j2 in range(t):
				if j1!=j2:
					resj1=None
					resj2=None
					symb=None
					for trans in transitions:#etats resultat de transitions inter Qj
						if trans[1] in Q[i]:
							if trans[2] in Q[j1] and (symb==None or symb==trans[0]):#le meme symbole a en cours (il faut qu'il y ai le meme symbole de transition pour les deux equivalences)
								resj1=j1
								symb=trans[0]
								
							if trans[2] in Q[j2] and (symb==None or symb==trans[0]):
								resj2=j2
								symb=trans[0]

					print(resj1,resj2)		
					if resj1 and resj2:
						return i,resj1,resj2,symb
							
					
		

	raise Exception("wtf error, text says that there should be j1 and j2")


class Automata:
	"""une classe représentant les automates, dont les transitions sont représentés comme ça :(caractere de transition, etat depart, etat arrivé)"""
	def __init__(self,Q,A,T,init,F):
		self.Q=Q#eensemble des etats
		self.A=A#alphabet
		self.T=T#liste des transitions (caractere de transition, etat depart, etat arrivé)
		self.init=init#etat initial
		self.F=F#etats finaux
		self.TabFinal=[False for n in Q]
		
		for q in F:
			self.TabFinal[q]=True
			
		#~ print(self.TabFinal)
		
		
	def minimisation(self):
		"""retourne un Automate minimum en utilisant l'algo Blum"""
		Q=[set(),self.F.copy(),self.Q-self.F]
		K,_,_,_=Ksetashdey.generateAll(self)
		print(K)
		input()
		t=2
		#~ i=1
		first=not_exists_transitions_between_classes(Q,self.T,self.A,t)
		while first:
			print(Q,first)
			#~ i,j1,j2,symbol=find_classes_indices(Q,self.T,t)
			#~ if len([trans for trans in self.T if trans[0]==symbol and trans[1] in Q[i] and trans[2] in Q[j1]])<=len([trans for trans in self.T if trans[0]==symbol and trans[1] in Q[i] and trans[2] in Q[j2]]):
				#~ Q.append(set([trans[2] for trans in self.T if trans[0]==symbol and trans[1] in Q[i] and trans[2] in Q[j1]]))
			#~ else:
				#~ Q.append(set([trans[2] for trans in self.T if trans[0]==symbol and trans[1] in Q[i] and trans[2] in Q[j2]]))
			#~ Q[i]=Q[i]-Q[t]
			#~ t+=1
			i,j,symb=first
			
			first=not_exists_transitions_between_classes(Q,self.T,self.A,t)
			
					
		print("terminé")
		print(Q)
		
		T=set([(car,s1,s2) for (car,st1,st2) in self.T for s1 in range(len(Q)) for s2 in range(len(Q)) if (s1!=s2 and st1 in Q[s1] and st2 in Q[s2])])
		Q=set([x for x in range(len(Q[1:]))])
		return Automata(Q[1:],self.A.copy(),T,self.init,self.F.copy())




def main():
	a=Automata(set([0,1,2,3,4]),set([0,1]),set([(0,0,1),(0,1,2),(1,1,0),(1,2,3),(0,3,4),(0,4,3)]),0,set([4]))
	print(a.minimisation())

main()
