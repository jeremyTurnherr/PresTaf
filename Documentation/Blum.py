
from random import *

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
		
	def __cmp__(self,o):
		return self.taille-o.taille
		
	def get_len(self):
		return self.taille
			
		
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
			
		def addEnd(self,maillon):
			if self.suiv==None:
				maillon.prec=self
				self.suiv=maillon
			else:
				self.suiv.addEnd(maillon)
		
		def addBeginning(self,maillon,d):
			self.prec=maillon
			maillon.suiv=self
			d.suiv=maillon
			
		def remove(self,lchaine):
			if self.listeChainee==lchaine:
				self.prec.suiv=self.suiv
				if self.suiv!=None:
					self.suiv.prec=self.prec
				
			else:
				if self.suiv:
					self.suiv.remove(lchaine)
				
		def __repr__(self):
			res="<<"+str(self.listeChainee)+">>"
			
			
			return res
			
	def __init__(self,i,a,K):
		self.i=i
		self.symb=a
		self.Kself=None#représente sa pos dans K
		self.suiv=None#premier elem
		
		self.isAdded=False
		
		self.taille=0
		
	def __contains__(self,o):
		temp=self.suiv
		while temp!=None:
			if temp.listeChainee==o:
				return True
			temp=temp.suiv
		return False
		
		
	def addListe(self,lchaine):
		"""tente d'ajouter la liste, retourne si ça a reussi (si len>=2"""
		
		maillon=self.DeltaMaillon(None,None,lchaine)
		if self.suiv==None:
			self.suiv=maillon
		else:
			self.suiv.addBeginning(maillon,self)
		self.taille+=1
		
	def removeListe(self,lchaine,K):
		"""retire la chaine, s'enleve de K si il devient trop petit"""
		self.suiv.remove(lchaine)
		self.taille-=1
		if self.taille<2:
			K.removeDeltaPrime(self.Kself)
		
	def can_be_added(self):
		return not self.isAdded and self.taille>=2
		
	def add_to_K(self,K):
		K.addDeltaPrime(self)
		self.isAdded=True
		
	def get_2_random_lists(self):
		"""prend les deux premieres"""
		return self.suiv,self.suiv.suiv
		
		
	def __repr__(self):
		res="{i "+str(self.i)+",a "+str(self.symb)+"}> "
		temp=self.suiv
		while temp:
			res+=str(temp)+"+"
			temp=temp.suiv
		return res
	
		
#~ class Ksetashdey:
	#~ """l'ensemble des delta prime de longueur >=2"""
	#~ def __init__(self):
		#~ self.K=[]
		#~ self.indicesdispos=[]
		#~ self.indiceafournir=0
		#~ self.nbDeltas=0
		#~ 
	#~ def addDeltaPrime(self,dprime):
		#~ dprime.Kself=self.indiceafournir
		#~ self.indiceafournir+=1
		#~ self.K.append(dprime)
		#~ self.indicesdispos.append(dprime.Kself)
		#~ self.nbDeltas+=1
		#~ 
	#~ def removeDeltaPrime(self,id):
		#~ self.K[id]=None
		#~ self.indicesdispos.remove(id)
		#~ self.nbDeltas-=1
		#~ 
	#~ def get_random_delta(self):
		#~ return self.K[choice(self.indicesdispos)]
		#~ 
	#~ def isEmpty(self):
		#~ return self.nbDeltas==0
		#~ 
	#~ def __repr__(self):
		#~ res="------"
		#~ for dp in self.K:
			#~ if dp:
				#~ res+="\n"+str(dp)
				#~ res+="\n------"
		#~ return res
		#~ 
	#~ 
	#~ 
	#~ @staticmethod
	#~ def generateAll(automata):
		#~ classes=[set(),automata.F.copy(),automata.Q-automata.F]
		#~ resK=Ksetashdey()
		#~ resDelta=Delta(len(automata.A),len(automata.Q))
		#~ resDeltaMoinsUn=DeltaMoinsUn(len(automata.Q),len(automata.A))
		#~ reslchaine=[[[ListeDoubleChainee(x,y,z) for x in range(3)] for y in automata.A] for z in range(3)]
		#~ resdeltaprime=[[DeltaPrime(i,a,resK) for a in automata.A] for i in automata.Q]
		#~ 
		#~ for t in automata.T:
			#~ t1f=1
			#~ t2f=1
			#~ if automata.TabFinal[t[1]]:
				#~ t1f=2
			#~ if automata.TabFinal[t[2]]:
				#~ t2f=2
			#~ 
			#~ mtemp=reslchaine[t2f][t[0]][t1f].add(t[1])
			#~ if reslchaine[t2f][t[0]][t1f] not in resdeltaprime[t1f][t[0]]:
				#~ resdeltaprime[t1f][t[0]].addListe(reslchaine[t2f][t[0]][t1f])
				#~ if resdeltaprime[t1f][t[0]].can_be_added():
					#~ resdeltaprime[t1f][t[0]].add_to_K(resK)
			#~ resDelta.mat[t[1]][t[0]]=mtemp
			#~ resDeltaMoinsUn.mat[t[0]][t[2]].append(t[1])
		#~ print("------------------------------------")
		#~ print(resDelta)
		#~ print("------------------------------------")
		#~ for i in reslchaine:
			#~ for y in i:
				#~ for z in y:
					#~ if z.deb:
						#~ print(z)
		#~ print("------------------------------------")
		#~ print(resDeltaMoinsUn)
		#~ print("------------------------------------KKKKK")
		#~ print(resK)
#~ 
		#~ 
					#~ 
		#~ input()
		#~ return resK,resDelta,reslchaine,resDeltaMoinsUn
		#~ 
class Ksetashdey:
	"""l'ensemble des delta prime de longueur >=2"""
	class KMaillon:
		def __init__(self,deltaPrime):
			self.prec=None
			self.suiv=None
			self.deltaPrime=deltaPrime
			
		def setPrec(prec):
			self.prec=prec
			
		def getPrec():
			return self.prec
			
		def setSuiv(suiv):
			self.suiv=suiv
			
		def Suiv():
			return self.suiv
			
		def remove(self,maillon):
			"""maillon est un deltaPrime"""
			if self.deltaPrime==maillon:
				if self.suiv!=None:
					self.prec.suiv=self.suiv
					self.suiv.prec=self.prec
				else:
					self.prec.suiv=None
			else:
				if self.suiv!=None:
					self.suiv.remove(maillon)
				else:
					print("Not found")
			
			
	
	def __init__(self):
		self.tete=None
		self.queue=None
		self.nbDelta=0
		
	def addDeltaPrime(self,dprime):
		d=self.KMaillon(dprime)
		if(self.queue==None):
			self.tete=d
			self.queue=d
		else:
			self.queue.setSuiv(d)
			d.setPrec(self.queue)
			self.queue=d
		self.nbDelta+=1
		return d
		
	def removeDeltaPrime(self,maillon):
		self.tete.remove(maillon)
		#~ if(maillon!=self.queue):
			#~ maillon.getSuiv().setPrec(maillon.getPrec())
		#~ else:
			#~ self.queue=maillon.getPrec()
		#~ if(maillon!=self.tete):
			#~ maillon.getPrec().setSuiv(maillon.getSuiv())
		#~ else:
			#~ self.tete=maillon.getSuiv()
		self.nbDelta-=1
		
	def get_delta(self):
		return self.tete
		
	def __repr__(self):
		res="------"
	#	for dp in self.K:
	#		if dp:
	#			res+="\n"+str(dp)
	#			res+="\n------"
		return res
		
	def get_random_delta(self):
		return self.tete.deltaPrime
		
	def isEmpty(self):
		return self.nbDelta==0
	
		
	
	
	@staticmethod
	def generateAll(automata):
		classes=[set(),automata.F.copy(),automata.Q-automata.F]
		resK=Ksetashdey()
		resDelta=Delta(len(automata.A),len(automata.Q))
		resDeltaMoinsUn=DeltaMoinsUn(len(automata.Q),len(automata.A))
		reslchaine=[[[ListeDoubleChainee(x,y,z) for x in range(3)] for y in automata.A] for z in range(3)]
		resdeltaprime=[[DeltaPrime(i,a,resK) for a in automata.A] for i in automata.Q]
		
		for t in automata.T:
			t1f=1
			t2f=1
			if automata.TabFinal[t[1]]:
				t1f=2
			if automata.TabFinal[t[2]]:
				t2f=2
			
			mtemp=reslchaine[t2f][t[0]][t1f].add(t[1])
			if reslchaine[t2f][t[0]][t1f] not in resdeltaprime[t1f][t[0]]:
				resdeltaprime[t1f][t[0]].addListe(reslchaine[t2f][t[0]][t1f])
				if resdeltaprime[t1f][t[0]].can_be_added():
					resdeltaprime[t1f][t[0]].add_to_K(resK)
			resDelta.mat[t[1]][t[0]]=mtemp
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
		print("------------------------------------KKKKK")
		print(resK)

		#~ ll=ListeDoubleChainee.generateDeb([set(),automata.F.copy(),automata.Q-automata.F],automata.A,automata.T)
		#~ for l in reslchaine:
			#~ for y in l:
				#~ for z in y:
					#~ resK.addListeChaine(z)
					
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
		K,delta,chaine,deltamoinsun=Ksetashdey.generateAll(self)
		
		t=2
		#~ i=1
		#~ first=not_exists_transitions_between_classes(Q,self.T,self.A,t)
		while not K.isEmpty():
			#~ print(Q,first)
			print(K)
			input("1")
			
			dptemp=K.get_random_delta()#un delta prime temporaire
			l1,l2=dptemp.get_2_random_lists()
			l1,l2=l1.listeChainee,l2.listeChainee
			print(l1,l2)
			input("2")
			if l1.taille>l2.taille:
				choix=l2
			else:
				choix=l1
			print(choix)
			
			input("3")
			choix.i=t+1
			dptemp.removeListe(choix,K)#retire la plus petite liste du delta prime
			
			newdp=DeltaPrime(t+1,choix.symb,K)
			newdp.addListe(choix)#ajout a la nouvelle liste dp t+1
			if newdp.can_be_added():#inutile pour l'instant car length=1
				newdp.add_to_K(K)
			
			
			#~ i,j1,j2,symbol=find_classes_indices(Q,self.T,t)
			#~ if len([trans for trans in self.T if trans[0]==symbol and trans[1] in Q[i] and trans[2] in Q[j1]])<=len([trans for trans in self.T if trans[0]==symbol and trans[1] in Q[i] and trans[2] in Q[j2]]):
				#~ Q.append(set([trans[2] for trans in self.T if trans[0]==symbol and trans[1] in Q[i] and trans[2] in Q[j1]]))
			#~ else:
				#~ Q.append(set([trans[2] for trans in self.T if trans[0]==symbol and trans[1] in Q[i] and trans[2] in Q[j2]]))
			#~ Q[i]=Q[i]-Q[t]
			#~ t+=1
			#~ i,j,symb=first
			#~ 
			#~ first=not_exists_transitions_between_classes(Q,self.T,self.A,t)
			
					
		print("terminé")
		print(Q)
		
		T=set([(car,s1,s2) for (car,st1,st2) in self.T for s1 in range(len(Q)) for s2 in range(len(Q)) if (s1!=s2 and st1 in Q[s1] and st2 in Q[s2])])
		Q=set([x for x in range(len(Q[1:]))])
		return Automata(Q[1:],self.A.copy(),T,self.init,self.F.copy())




def main():
	a=Automata(set([0,1,2,3,4]),set([0,1]),set([(0,0,1),(1,0,3),(0,1,2),(1,1,0),(1,2,3),(0,3,4),(0,4,3)]),0,set([4]))
	print(a.minimisation())

main()
