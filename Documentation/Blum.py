

def exists_transitions_between_classes(classes,transitions):
	"""fonction qui teste si il y a des transitions entre une classe et une liste de classe"""
	
	for i in range(len(classes)):
		classe=classes[i]
		autres_classes=[classes[j] for j in range(len(classes)) if j!=i]
		for autre in autres_classes:
			for transition in transitions:
				if (transition[1] in classe and transition[2] in autre) or (transition[1] in autre and transition[2] in classe):#si il existe une transition d'une classe vers une autre
					return True
		

	return False
	
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
		
		
	def minimisation(self):
		"""retourne un Automate minimum en utilisant l'algo Blum"""
		t=2
		Q=[set(),self.F.copy(),self.Q-self.F]
		i=1
		while exists_transitions_between_classes(Q,self.T):
			i,j1,j2,symbol=find_classes_indices(Q,self.T,t)
			if len([trans for trans in self.T if trans[0]==symbol and trans[1] in Q[i] and trans[2] in Q[j1]])<=len([trans for trans in self.T if trans[0]==symbol and trans[1] in Q[i] and trans[2] in Q[j2]]):
				Q.append(set([trans[2] for trans in self.T if trans[0]==symbol and trans[1] in Q[i] and trans[2] in Q[j1]]))
			else:
				Q.append(set([trans[2] for trans in self.T if trans[0]==symbol and trans[1] in Q[i] and trans[2] in Q[j2]]))
			Q[i]=Q[i]-Q[t]
			t+=1
					
		print("terminé")
		print(Q)
		
		T=set([(car,s1,s2) for (car,st1,st2) in self.T for s1 in range(len(Q)) for s2 in range(len(Q)) if (s1!=s2 and st1 in Q[s1] and st2 in Q[s2])])
		Q=set([x for x in range(len(Q[1:]))])
		return Automata(Q[1:],self.A.copy(),T,self.init,self.F.copy())




def main():
	a=Automata(set([0,1,2,3,4]),set(["a","b"]),set([("a",0,1),("a",1,2),("b",1,0),("b",2,3),("a",3,4),("a",4,3)]),0,set([2]))
	print(a.minimisation())

main()
