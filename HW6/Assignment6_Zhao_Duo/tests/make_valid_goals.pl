#!/usr/bin/perl
open VOUTFILE, ">valid.valid";
open IOUTFILE, ">valid.invalid";

system("rm valid.valid.dict");
system("rm valid.invalid.dict");
open INFILE, "<all_on_board.invalid";
while (my $line = <INFILE>) {
    $line =~ s/all_on_board/valid/;
    print IOUTFILE $line;
}
close INFILE;

system("cat all_on_board.invalid.dict >> valid.invalid.dict");

open INFILE, "<conflict_free.invalid";
while (my $line = <INFILE>) {
    $line =~ s/conflict_free/valid/;
    print IOUTFILE $line;
}
close INFILE;

system("cat conflict_free.invalid.dict >> valid.invalid.dict");

open INFILE, "<conflict_free.valid";
while (my $line = <INFILE>) {
    $line =~ s/conflict_free/valid/;
    print VOUTFILE $line;
}
close INFILE;

system("cat conflict_free.valid.dict >> valid.valid.dict");

open IOUTFILE2, ">>valid.invalid.dict";

print IOUTFILE "valid(set(ship(5, horizontal, 1, 1), ship(5, horizontal, 1, 2), ship(3, horizontal, 1, 3), ship(3, horizontal, 1, 4), ship(2, horizontal, 1, 5))).\n";
print IOUTFILE2 "Bad lengths: 5, 5, 3, 3, 2\n";
print IOUTFILE "valid(set(ship(6, horizontal, 1, 1), ship(4, horizontal, 1, 2), ship(3, horizontal, 1, 3), ship(3, horizontal, 1, 4), ship(2, horizontal, 1, 5))).\n";
print IOUTFILE2 "Bad lengths: 6, 4, 3, 3, 2\n";
print IOUTFILE "valid(set(ship(4, horizontal, 1, 1), ship(5, horizontal, 1, 2), ship(3, horiztonal, 1, 3), ship(3, horizontal, 1, 4), ship(2, horizontal, 1, 5))).\n";
print IOUTFILE2 "Bad lengths: 4, 5, 3, 3, 2\n";

close IOUTFILE2;
close IOUTFILE;
close VOUTFILE;
