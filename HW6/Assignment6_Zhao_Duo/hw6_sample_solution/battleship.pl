%Valid orientations.
orientation(horizontal).  orientation(vertical).

%Valid columns
column(1).  column(2).  column(3).  column(4).  column(5).  column(6).
column(7).  column(8).  column(9).  column(10).

% Valid rows
row(1).  row(2).  row(3).  row(4).  row(5).  row(6).  row(7).  row(8).  row(9).
row(10).

% Whether a ship(length, orientation, column, row) is on the board.
on_board(ship(L, horizontal, X, Y)) :- column(X),
                                       row(Y),
                                       T is X + L - 1,
                                       T =< 10,
                                       Y =< 10.

on_board(ship(L, vertical, X, Y)) :- column(X),
                                     row(Y),
                                     X =< 10,
                                     T is Y + L - 1,
                                     T =< 10.

% Whether all ships are on board.
all_on_board(set(A, B, C, D, E)) :- on_board(A),
                                    on_board(B),
                                    on_board(C),
                                    on_board(D),
                                    on_board(E).

% Whether two ships are clear of each other.
% Horizontal and on different rows
clear(ship(_, horizontal, _, Y1), ship(_, horizontal, _, Y2)) :-
        Y1 =\= Y2.
% Falls through => same row
clear(ship(_, horizontal, X1, _), ship(L2, horizontal, X2, _)) :-
        T2 is X2 + L2 - 1,
        X1 > T2.
clear(ship(L1, horizontal, X1, _), ship(_, horizontal, X2, _)) :-
        T1 is X1 + L1 - 1,
        X2 > T1.

% Vertical and in different columns
clear(ship(_, vertical, X1, _), ship(_, vertical, X2, _)) :-
        X1 =\= X2.
% Falls through => same row
clear(ship(_, vertical, _, Y1), ship(L2, vertical, _, Y2)) :-
        T2 is Y2 + L2 - 1,
        Y1 > T2.
clear(ship(L1, vertical, _, Y1), ship(_, vertical, _, Y2)) :-
        T1 is Y1 + L1 - 1,
        Y2 > T1.

% First horizontal, second vertical
% Horizontal is above vertical
clear(ship(_, horizontal, _, Y1), ship(_, vertical, _, Y2)) :-
        Y1 < Y2.
% Horizontal is below vertical
clear(ship(_, horizontal, _, Y1), ship(L2, vertical, _, Y2)) :-
        T is Y2 + L2 - 1,
        Y1 > T.

% Vertical is to left of horizontal
clear(ship(_, horizontal, X1, _), ship(_, vertical, X2, _)) :-
        X2 < X1.
% Vertical is to right of horizontal
clear(ship(L1, horizontal, X1, _), ship(_, vertical, X2, _)) :-
        T is X1 + L1 - 1,
        X2 > T.

% When vertical first, just swap.
clear(ship(L1, vertical, X1, Y1), ship(L2, horizontal, X2, Y2)) :-
        clear(ship(L2, horizontal, X2, Y2), ship(L1, vertical, X1, Y1)).

% Whether it is possible for all of 5 ships to be conflict-free.
conflict_free(set(A, B, C, D, E)) :- clear(A, B),
                                     clear(A, C),
                                     clear(A, D),
                                     clear(A, E),
                                     clear(B, C),
                                     clear(B, D),
                                     clear(B, E),
                                     clear(C, D),
                                     clear(C, E),
                                     clear(D, E).

% Check for hit on horizontal ship.
hit(X, Y, ship(L, horizontal, Xs, Ys)) :- T is Xs + L - 1,
                                          Y =:= Ys,
                                          X >= Xs,
                                          X =< T.

% Check for hit on vertical ship.
hit(X, Y, ship(L, vertical, Xs, Ys)) :- T is Ys + L - 1,
                                        X =:= Xs,
                                        Y >= Ys,
                                        Y =< T.

% Check for hit on all ships.
hit(X, Y, set(S, _, _, _, _)) :- hit(X, Y, S).
hit(X, Y, set(_, S, _, _, _)) :- hit(X, Y, S).
hit(X, Y, set(_, _, S, _, _)) :- hit(X, Y, S).
hit(X, Y, set(_, _, _, S, _)) :- hit(X, Y, S).
hit(X, Y, set(_, _, _, _, S)) :- hit(X, Y, S).

% Checks for legal arrangement of 5 ships.
valid_base(S) :- conflict_free(S),
                 all_on_board(S),
                 print_board(S).

% Whether a full arrangement is valid.
valid(set(ship(5, O1, X1, Y1), ship(4, O2, X2, Y2), ship(3, O3, X3, Y3),
               ship(3, O4, X4, Y4), ship(2, O5, X5, Y5))) :-
        orientation(O1), orientation(O2), orientation(O3), orientation(O4),
        orientation(O5),
        column(X1), column(X2), column(X3), column(X4), column(X5),
        row(Y1), row(Y2), row(Y3), row(Y4), row(Y5),
        valid_base(set(ship(5, O1, X1, Y1),
                       ship(4, O2, X2, Y2),
                       ship(3, O3, X3, Y3),
                       ship(3, O4, X4, Y4),
                       ship(2, O5, X5, Y5))).

% Recursive "loop" to print out a row.
print_val(11, _, _) :- nl.

print_val(X, Y, S) :- X < 11,
                      hit(X, Y, S),
                      write('X'), !,
                      N is X + 1,
                      print_val(N, Y, S).

print_val(X, Y, S) :- X < 11,
                      write('-'), !,
                      N is X + 1,
                      print_val(N, Y, S).

print_line(11, _) :- nl.

print_line(Y, S) :- Y < 11,
                    print_val(1, Y, S), !,
                    N is Y + 1,
                    print_line(N, S).

% Finally print board.
print_board(S) :- print_line(1, S),nl,nl.
