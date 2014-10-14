#!/usr/bin/perl
open OUTFILE, ">database.valid";
for (my $i = 1; $i <= 10; $i++){
    print OUTFILE "row($i).\n";
    print OUTFILE "column($i).\n";
}
print OUTFILE "orientation(vertical).\n";
print OUTFILE "orientation(horizontal).\n";
close OUTFILE;

open OUTFILE2, ">database.invalid";
my @bad_row_column = ("0", "-1", "11", "100", "vertical", "horizontal");
foreach $item (@bad_row_column) {
    print OUTFILE2 "row($item).\n";
    print OUTFILE2 "column($item).\n";
}
close OUTFILE2;

system("cp database.valid database.valid.dict");
system("cp database.invalid database.invalid.dict");
