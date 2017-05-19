digraph system { 
I -> C0I0
C0I0 [label=0]
C0I0->C1I1 [label=0]
C0I0->C0I1 [label=1]
C0I1 [label=1]
C0I1->C0I0 [label=0]
C0I1->zero [label=1]
C1I0 [label=A2]
C1I0->C1I2 [label=0]
C1I0->C1I1 [label=1]
C1I1 [label=3]
C1I1->zero [label=0]
C1I1->C1I0 [label=1]
C1I2 [label=A4]
C1I2->C1I0 [label=0]
C1I2->zero [label=1]
}
