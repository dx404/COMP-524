% 1) 
orientation(vertical). orientation(horizontal). 
column(1). column(2). column(3). column(4). column(5). column(6). column(7). column(8). column(9). column(10). 
row(1). row(2). row(3). row(4). row(5). row(6). row(7). row(8). row(9). row(10). 
length(5). length(4). length(3). length(2).

% 2)
ship(Length, Orientation, Column, Row) 
	:- length(Length), orientation(Orientation), column(Column), row(Row). 

% checking if a set is valid
set(
	ship(5, Orientation1, Column1, Row1), 
	ship(4, Orientation2, Column2, Row2), 
	ship(3, Orientation3, Column3, Row3), 
	ship(3, Orientation4, Column4, Row4),
	ship(2, Orientation5, Column5, Row5)
	):- 
	ship(5, Orientation1, Column1, Row1), 
	ship(4, Orientation2, Column2, Row2), 
	ship(3, Orientation3, Column3, Row3), 
	ship(3, Orientation4, Column4, Row4),
	ship(2, Orientation5, Column5, Row5). 

% checking if a single ship is on board
on_board(ship(Length, vertical, _, Row)) :- 
		Row > 0, Row + Length =< 11, !.
		
on_board(ship(Length, horizontal, Column, _)) :- 
		Column > 0, Column + Length =< 11.
		
all_on_board(
 		set(
			ship(5, Orientation1, Column1, Row1), 
			ship(4, Orientation2, Column2, Row2), 
			ship(3, Orientation3, Column3, Row3), 
			ship(3, Orientation4, Column4, Row4),
			ship(2, Orientation5, Column5, Row5)
		)
	) :-
		on_board(ship(5, Orientation1, Column1, Row1)), 
		on_board(ship(4, Orientation2, Column2, Row2)), 
		on_board(ship(3, Orientation3, Column3, Row3)), 
		on_board(ship(3, Orientation4, Column4, Row4)), 
		on_board(ship(2, Orientation5, Column5, Row5)).

% 3) 	
mutual_conflict_free(
			ship(Length1, vertical, Column1, Row1),
			ship(Length2, vertical, Column2, Row2)
		) :- (
			Column1 =\= Column2; 
			Column1 =:= Column2, (
				Row1 + Length1 -1 < Row2; 
				Row2 + Length2 -1 < Row1
			)), !. 
			
mutual_conflict_free(
			ship(Length1, horizontal, Column1, Row1),
			ship(Length2, horizontal, Column2, Row2)
		) :-( 
			Row1 =\= Row2; 
			Row1 =:= Row2, (
				Column1 + Length1 -1 < Column2;
				Column2 + Length2 -1 < Column1
			)),!.

mutual_conflict_free(
			ship(Length1, vertical, Column1, Row1),
			ship(Length2, horizontal, Column2, Row2)
		) :- ( 
			Column1 < Column2 ;
			Column1 > Column2 + Length2 -1 ;
			Row2 < Row1 ;
			Row2 > Row1 + Length1 - 1), !. 

mutual_conflict_free(
			ship(Length1, horizontal, Column1, Row1),
			ship(Length2, vertical, Column2, Row2)
		) :- (
			Column2 < Column1;
			Column2 > Column1 + Length1 -1;
			Row1 < Row2;
			Row1 > Row2 + Length2 - 1), !.
			 
conflict_free(
	set(
		ship(5, Orientation1, Column1, Row1), 
		ship(4, Orientation2, Column2, Row2), 
		ship(3, Orientation3, Column3, Row3), 
		ship(3, Orientation4, Column4, Row4),
		ship(2, Orientation5, Column5, Row5)
		)
	) :- 
		mutual_conflict_free(
				ship(5, Orientation1, Column1, Row1), 
				ship(4, Orientation2, Column2, Row2)
			),
		mutual_conflict_free(
				ship(5, Orientation1, Column1, Row1),
				ship(3, Orientation3, Column3, Row3)
			),
		mutual_conflict_free(
				ship(5, Orientation1, Column1, Row1), 
				ship(3, Orientation4, Column4, Row4)
			),
		mutual_conflict_free(
				ship(5, Orientation1, Column1, Row1),
				ship(2, Orientation5, Column5, Row5)
			),
		mutual_conflict_free(
				ship(4, Orientation2, Column2, Row2), 
				ship(3, Orientation3, Column3, Row3)
			),
		mutual_conflict_free(
				ship(4, Orientation2, Column2, Row2),
				ship(3, Orientation4, Column4, Row4)
			),
		mutual_conflict_free(
				ship(4, Orientation2, Column2, Row2), 
				ship(2, Orientation5, Column5, Row5)
			),
		mutual_conflict_free(
				ship(3, Orientation3, Column3, Row3),
				ship(3, Orientation4, Column4, Row4)
			),
		mutual_conflict_free(
				ship(3, Orientation3, Column3, Row3), 
				ship(2, Orientation5, Column5, Row5)
			),
		mutual_conflict_free(
				ship(3, Orientation4, Column4, Row4),
				ship(2, Orientation5, Column5, Row5)
			).
% 4) and 7)
valid(
	set(
		ship(5, Orientation1, Column1, Row1), 
		ship(4, Orientation2, Column2, Row2), 
		ship(3, Orientation3, Column3, Row3), 
		ship(3, Orientation4, Column4, Row4),
		ship(2, Orientation5, Column5, Row5)
		)
	) :- 
		orientation(Orientation1), 
		orientation(Orientation2), 
		orientation(Orientation3), 
		orientation(Orientation4), 
		orientation(Orientation5), 
		column(Column1), 
		column(Column2), 
		column(Column3), 
		column(Column4), 
		column(Column5), 
		row(Row1), 
		row(Row2), 
		row(Row3), 
		row(Row4), 
		row(Row5),
		all_on_board(
 			set(
				ship(5, Orientation1, Column1, Row1), 
				ship(4, Orientation2, Column2, Row2), 
				ship(3, Orientation3, Column3, Row3), 
				ship(3, Orientation4, Column4, Row4),
				ship(2, Orientation5, Column5, Row5)
			)
		), 
		conflict_free(
			set(
				ship(5, Orientation1, Column1, Row1), 
				ship(4, Orientation2, Column2, Row2), 
				ship(3, Orientation3, Column3, Row3), 
				ship(3, Orientation4, Column4, Row4),
				ship(2, Orientation5, Column5, Row5)
			)
		),
		loopRow(1, set(
			ship(5, Orientation1, Column1, Row1), 
			ship(4, Orientation2, Column2, Row2), 
			ship(3, Orientation3, Column3, Row3), 
			ship(3, Orientation4, Column4, Row4),
			ship(2, Orientation5, Column5, Row5)
			)
		).



% 5)
hit(X, Y, ship(Length, vertical, Column, Row)) :- 
	X =:= Column, 
	Y >= Row,
	Y =< Row + Length -1, !. 
	
hit(X, Y, ship(Length, horizontal, Column, Row)) :- 
	Y =:= Row, 
	X >= Column,
	X =< Column + Length -1. 

hit(X, Y, set(ship(Length, Orientation, Column, Row), _, _, _, _)) :-
		hit(X, Y, ship(Length, Orientation, Column, Row)), !. 
		
hit(X, Y, set(_, ship(Length, Orientation, Column, Row), _, _, _)) :-
		hit(X, Y, ship(Length, Orientation, Column, Row)), !. 
		
hit(X, Y, set(_, _, ship(Length, Orientation, Column, Row), _, _)) :-
		hit(X, Y, ship(Length, Orientation, Column, Row)), !. 
		
hit(X, Y, set(_, _, _, ship(Length, Orientation, Column, Row), _)) :-
		hit(X, Y, ship(Length, Orientation, Column, Row)), !. 
		
hit(X, Y, set(_, _, _, _, ship(Length, Orientation, Column, Row))) :-
		hit(X, Y, ship(Length, Orientation, Column, Row)). 
		

% 6)

loopInRow(11, _, _) :- !.

loopInRow(Column, Row, set(
		ship(5, Orientation1, Column1, Row1), 
		ship(4, Orientation2, Column2, Row2), 
		ship(3, Orientation3, Column3, Row3), 
		ship(3, Orientation4, Column4, Row4),
		ship(2, Orientation5, Column5, Row5)
			) ) :-
	Column =< 10, posPrint(Column, Row, set(
		ship(5, Orientation1, Column1, Row1), 
		ship(4, Orientation2, Column2, Row2), 
		ship(3, Orientation3, Column3, Row3), 
		ship(3, Orientation4, Column4, Row4),
		ship(2, Orientation5, Column5, Row5)
			) 
		), NewColumn is Column + 1, 
	loopInRow(NewColumn, Row, set(
		ship(5, Orientation1, Column1, Row1), 
		ship(4, Orientation2, Column2, Row2), 
		ship(3, Orientation3, Column3, Row3), 
		ship(3, Orientation4, Column4, Row4),
		ship(2, Orientation5, Column5, Row5)
			) ).

loopRow(11, _) :- !.
loopRow(Row, set(
		ship(5, Orientation1, Column1, Row1), 
		ship(4, Orientation2, Column2, Row2), 
		ship(3, Orientation3, Column3, Row3), 
		ship(3, Orientation4, Column4, Row4),
		ship(2, Orientation5, Column5, Row5)
			) 
		) :- 
	Row =< 10, 
	loopInRow(1, Row, set(
		ship(5, Orientation1, Column1, Row1), 
		ship(4, Orientation2, Column2, Row2), 
		ship(3, Orientation3, Column3, Row3), 
		ship(3, Orientation4, Column4, Row4),
		ship(2, Orientation5, Column5, Row5)
			) 
		), nl, NewRow is Row + 1, 
		loopRow(NewRow, set(
		ship(5, Orientation1, Column1, Row1), 
		ship(4, Orientation2, Column2, Row2), 
		ship(3, Orientation3, Column3, Row3), 
		ship(3, Orientation4, Column4, Row4),
		ship(2, Orientation5, Column5, Row5)
				) 
		).

% used to print 'X' or '-' 
posPrint(X, Y, set(
		ship(5, Orientation1, Column1, Row1), 
		ship(4, Orientation2, Column2, Row2), 
		ship(3, Orientation3, Column3, Row3), 
		ship(3, Orientation4, Column4, Row4),
		ship(2, Orientation5, Column5, Row5)
			) 
		):- 
		hit(X, Y, set(
				ship(5, Orientation1, Column1, Row1), 
				ship(4, Orientation2, Column2, Row2), 
				ship(3, Orientation3, Column3, Row3), 
				ship(3, Orientation4, Column4, Row4),
				ship(2, Orientation5, Column5, Row5)
			)
		), 
		write('X'), !;
		write('-').

% 7) 
% include in the 4) valid
% It may take about 1 min to get the following output, but subsequent ones are achieved instantly. 
% appreciate your patience here....
%
%
%?- valid(set(ship(5, O1, C1, R1), ship(4, O2, C2, R2), ship(3, O3, C3, R3), ship(3, O4, C4, R4), ship(2, O5, C5, R5))).
% XX--------
% XX--------
% XX--------
% XX--------
% XX--------
% XX--------
% XX--------
% XX--------
% X---------
% ----------
% O1 = vertical,
% C1 = 1,
% R1 = 1,
% O2 = vertical,
% C2 = 1,
% R2 = 6,
% O3 = vertical,
% C3 = 2,
% R3 = 1,
% O4 = vertical,
% C4 = 2,
% R4 = 4,
% O5 = vertical,
% C5 = 2,
% R5 = 7 ;
% XX--------
% XX--------
% XX--------
% XX--------
% XX--------
% XX--------
% X---------
% XX--------
% XX--------
% ----------
% O1 = vertical,
% C1 = 1,
% R1 = 1,
% O2 = vertical,
% C2 = 1,
% R2 = 6,
% O3 = vertical,
% C3 = 2,
% R3 = 1,
% O4 = vertical,
% C4 = 2,
% R4 = 4,
% O5 = vertical,
% C5 = 2,
% R5 = 8 


