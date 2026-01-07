## Introduction

This is an archive of some code I wrote for a university class. Both programs are meant to be run in the terminal.

## ADFA_Decidability.java
ADFA_Decidability.java illustrates the decidability of A<sub>DFA</sub>, where A<sub>DFA</sub> = {<B, w> | B is a DFA that accepts input string w}. When the user provides an input DFA and string, the program will return True
if the DFA accepts the string and False if it doesn't.

The input DFA and string must be formatted in a specific way or the program will return False. The DFA must be listed before the string, and they must be separated by a hyphen. If needed, the symbol # is used to represent the
empty string. The different parts of the DFA must be listed in the order: states, alphabet, transition functions, start state, then accept states. These parts must be separated from each other using colons. When listed, the
states, elements of the alphabet, and accept states must be separated by commas. All transitions in the transition function must be listed in the format q1,e>q2 such that ((q1,e)) = q2. These transitions are separated using
semi-colons. To give a clarifying example, the string "q0,q1:0,1:q0,0>q1;q0,1>q1;q1,0>q0;q1,1>q0:q0:q1-0011" represents the DFA with the state diagram
<div align="center"><img width="225" height="225" alt="CSE 105 DFA Diagram 1" src="https://github.com/user-attachments/assets/a40a537c-a2ef-4938-834f-fa7ca246ddb2" /></div>
and the string "0011".

## ATM_HALTTM_Mapping_Reduction.java

ATM_HALTTM_Mapping_Reduction.java illustrates the mapping reduction A<sub>TM</sub> &#x2264;<sub>M</sub> HALT<sub>TM</sub>, where A<sub>TM</sub> = {<M, w> | M is a Turing Machine that accepts input string w} and
HALT<sub>TM</sub> = {<M, w> | M is a Turing Machine that halts on input string w}. It does so by implementing the computable function F: &#931;* -> &#931;* defined as  

<code>F(x) = {const<sub>out</sub> if x &#8800; <M, w> for any Turing Machine M and string w over the alphabet of M  
&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;{<M'<sub>x</sub>, w> if x = <M, w> for some Turing Machine M and string w over the alphabet of M
</code>

where M'<sub>x</sub> is a Turing Machine that computes like M except, if the computation of M were to go to a reject state, M'<sub>x</sub> loops instead. Meanwhile, const<sub>out</sub> is the Turing Machine represented by the state diagram  
<div align="center"><img width="559" height="300" alt="CSE 105 TM Diagram 1" src="https://github.com/user-attachments/assets/9cad6f75-0bba-4c30-a186-379d8a7088ae" /></div>  
and the empty string.<br><br>

The user will provide the input x and the program will output F(x).<br>

Similarly to the previous program, Turing Machines and their associated string are formatted in a specific way. The Turing Machine must be listed before the string, and they must be separated by a hyphen. # is the
symbol used to represent the empty string and the blank symbol; thus, it cannot be included in the input alphabet. The different parts of the Turing Machine must be listed in the order: states, input alphabet, tape alphabet, transition function, start state,
accept state, then reject state. These parts must be separated from each other using colons. When listed, the states, elements of the input alphabet, and elements of the tape alphabet must be separated with commas.
In addition, “qloop” cannot be the name of a state, since that state will be added later to create M’<sub>x</sub>. All transitions in the transition function must be listed in the format q1,#>q2,#,r such that δ((q1,#)) = (q2,#,r).
These transitions are separated by semi-colons. Any transition not listed will output (qr,#,r) where qr is the reject state as convention states. To give a clarifying example, the string
“q0,qa,qr:0,1:0,1,#:q0,0>q0,#,r;q0,1>q0,#,r;q0,#>q0,#,r:q0:qa:qr-#” represents const<sub>out</sub>.
