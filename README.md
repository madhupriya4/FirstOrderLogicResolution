# FirstOrderLogicResolution
First order resolution to check whether a query is entailed in a given knowledge base.

Format	for	input.txt:	
	
<NQ = NUMBER OF QUERIES>
<QUERY 1>
...
<QUERY NQ>
<NS = NUMBER OF GIVEN SENTENCES IN THE KNOWLEDGE BASE>
<SENTENCE 1>
...
<SENTENCE NS>
	
where	
	
• Each	query	will	be	a	single	literal	of	the	form	Predicate(Constant)	or	~Predicate(Constant).	
• Variables	are	all	single	lowercase	letters.	
• All	predicates	(such	as	Sibling)	and	constants	(such	as	John)	are	case-sensitive	alphabetical	strings	that	
begin	with	an	uppercase	letter.		
• Each	predicate	takes	at	least	one	argument.	Predicates	will	take	at	most	100	arguments.	A	given	
predicate	name	will	not	appear	with	different	number	of	arguments.	
• There	will	be	at	most	100	queries	and	1000	sentences	in	the	knowledge	base.		
• See	the	sample	input	below	for	spacing	patterns.		
• You	can	assume	that	the	input	format	is	exactly	as	it	is	described.	There	will	be	no	syntax	errors	in	the	
given	input.		
Format	for	output.txt:	
	
For	each	query,	determine	if	that	query	can	be	inferred	from	the	knowledge	base	or	not,	one	query	per	line:	
	
<ANSWER 1>
...
<ANSWER NQ>
	
where	
	
each	answer	should	be	either	TRUE	if	you	can	prove	that	the	corresponding	query	sentence	is	true	given	the	
knowledge	base,	or	FALSE	if	you	cannot.	
	
Notes	and	hints:	
	
- Please	 name	 your	 program	 “homework.xxx”	 where	 ‘xxx’	 is	 the	 extension	 for	 the	 programming	
language	you	choose.	(“py”	for	python,	“cpp”	for	C++,	and	“java”	for	Java).	If	you	are	using	C++11,	then	
the	name	of	your	file	should	be	“homework11.cpp”	and	if	you	are	using	python3.4	then	the	name	of	
your	file	should	be	“homework3.py”.	
-
If	you	decide	that	the	given	statement	can	be	inferred	from	the	knowledge	base,	every	variable	in	each	
sentence	used	in	the	proving	process	should	be	unified	with	a	Constant	(i.e.,	unify	variables	to	
constants	before	you	trigger	a	step	of	resolution).	-
All	variables	are	assumed	to	be	universally	quantified.	There	is	no	existential	quantifier	in	this	
homework.	There	is	no	need	for	Skolem	functions	or	Skolem	constants.	
-
Operator	priorities	apply	(negation	has	higher	priority	than	disjunction).	There	will	be	no	parentheses	
in	the	sentences,	other	than	around	arguments	of	predicates.	
-
The	knowledge	base	that	you	will	be	given	is	consistent.	So	there	are	no	contracting	rules	or	facts	in	
the	knowledge	base.		
-
If	you	run	into	a	loop	and	there	is	no	alternative	path	you	can	try,	report	FALSE.	An	example	for	this	
would	be	having	two	rules	(1)	~A(x)	|	B(x)	and	(2)	~B(x)	|	A(x)		and	wanting	to	prove	A(John).	In	this	
case	your	program	should	report	FALSE.		
	
	
Example	1:	
	
For	this	input.txt:	
6
F(Joe)
H(John)
~H(Alice)
~H(John)
G(Joe)
G(Tom)
14
~F(x) | G(x)
~G(x) | H(x)
~H(x) | F(x)
~R(x) | H(x)
~A(x) | H(x)
~D(x,y) | ~H(y)
~B(x,y) | ~C(x,y) | A(x)
B(John,Alice)
B(John,Joe)
~D(x,y) | ~Q(y) | C(x,y)
D(John,Alice)
Q(Joe)
D(John,Joe)
R(Tom)
your	output.txt	should	be:	
FALSE
TRUE
TRUE
FALSE
FALSE
TRUE  
Example	2:	
	
For	this	input.txt:	
	
2
Ancestor(Liz,Billy)
Ancestor(Liz,Joe)
6
Mother(Liz,Charley)
Father(Charley,Billy)
~Mother(x,y) | Parent(x,y)
~Father(x,y) | Parent(x,y)
~Parent(x,y) | Ancestor(x,y)
~Parent(x,y) | ~Ancestor(y,z) | Ancestor(x,z)
	
your	output.txt	should	be:	
	
TRUE
FALSE
	
Example	3:	
	
For	this	input.txt:	
	
1
Criminal(West)
6
~American(x) | ~Weapon(y) | ~Sells(x,y,z) | ~Enemy(z,America) | Criminal(x)
Owns(Nono,M1)
Missile(M1)
~Missile(x) | ~Owns(Nono,x) | Sells(West,x,Nono)
~Missile(x) | Weapon(x)
Enemy(Nono,America)
	
your	output.txt	should	be:	
	
TRUE
