


class Automata:
	
	def __init__(self,Q,A,T,init,F):
		self.Q=Q#eensemble des etats
		self.A=A#alphabet
		self.T=T#liste des transitions
		self.init=init#etat initial
		self.F=F#etats finaux
		
		
	def minimisation(self):
		"""retourne un Automate minimum en utilisant l'algo Blum"""
		t=2
		Q=[set(),self.F.copy(),self.Q-self.F]
		i=1
		while i<=t and self.A:
			pass
