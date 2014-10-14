#!/usr/bin/perl
open OUTFILE, ">all_on_board.valid";
open OUTFILE2, ">all_on_board.valid.dict";

# Were not required to check ship lengths, but not taking off if students did.
my @lengths = (5, 4, 3, 3, 2);

sub set_string {
    my $ret = "all_on_board(set(" . join(", ", @_) . ")).\n";
}

sub left {
    return 1;
}

sub top {
    return 1;
}

sub right {
    my @arglist = @_;
    my $length = shift @arglist;
    die if $length == 0;
    my $orientation = shift @arglist;
    if ($orientation eq "vertical") {
        return 10;
    }
    elsif ($orientation eq "horizontal") {
        my $ret = 10 - $length + 1;
        return $ret;
    }
}

sub bottom {
    my @arglist = @_;
    my $length = shift @arglist;
    die if $length == 0;
    my $orientation = shift @arglist;
    if ($orientation eq "vertical") {
        my $ret = 10 - $length + 1;
        return $ret;
    }
    elsif ($orientation eq "horizontal") {
        return 10;
    }
}

sub ship {
    my @arglist = @_;
    my $length = shift @arglist;
    die if $length == 0;
    my $orientation = shift @arglist;
    my $x = shift @arglist;
    my $y = shift @arglist;
    return "ship($length, $orientation, $x, $y)";
}

#All in left corner, vertical
my @set = ();
foreach $length (@lengths) {
    my @tuple = ($length, "vertical");
    push @set, ship(@tuple, left(@tuple), top(@tuple));
}
print OUTFILE set_string(@set);
print OUTFILE2 "All left corner, vertical\n";

#All in left corner, horizontal
@set = ();
foreach $length (@lengths) {
    my @tuple = ($length, "horizontal");
    push @set, ship(@tuple, left(@tuple), top(@tuple));
}
print OUTFILE set_string(@set);
print OUTFILE2 "All in left corner, horizontal\n";

#Corresponding rows/columns, vertical
@set = ();
for (my $i = 0; $i < @lengths; $i++) {
    my @tuple = ($lengths[$i], "vertical");
    push @set, ship(@tuple, $i+1, top(@tuple));
}
print OUTFILE set_string(@set);
print OUTFILE2 "Corresponding columns, vertical\n";

#Corresponding rows/columns, horizontal
@set = ();
for (my $i = 0; $i < @lengths; $i++) {
    my @tuple = ($lengths[$i], "horizontal");
    push @set, ship(@tuple, left(@tuple), $i + 1);
}
print OUTFILE set_string(@set);
print OUTFILE2 "Corresponding rows, horizontal\n";

# Bottom right corner, horizontal
@set = ();
foreach $length (@lengths) {
    my @tuple = ($length, "horizontal");
    push @set, ship(@tuple, right(@tuple), bottom(@tuple));
}
print OUTFILE set_string(@set);
print OUTFILE2 "Bottom right corner, horizontal\n";

# Bottom right corner, vertical
@set = ();
foreach $length (@lengths) {
    my @tuple = ($length, "vertical");
    push @set, ship(@tuple, right(@tuple), bottom(@tuple));
}
print OUTFILE set_string(@set);
print OUTFILE2 "Bottom right corner, vertical\n";

# Bottom right corner, separate rows, horizontal
@set = ();
for (my $i = 0; $i < @lengths; $i++) {
    my @tuple = ($lengths[$i], "horizontal");
    push @set, ship(@tuple, right(@tuple), 10 - $i);
}
print OUTFILE set_string(@set);
print OUTFILE2 "Bottom right corner, separate rows, horizontal\n";

# Bottom right corner, separate columns, vertical
@set = ();
for (my $i = 0; $i < @lengths; $i++) {
    my @tuple = ($lengths[$i], "vertical");
    push @set, ship(@tuple, 10 - $i, bottom(@tuple));
}
print OUTFILE set_string(@set);
print OUTFILE2 "Bottom right corner, separate columns, vertical\n";

close OUTFILE;
close OUTFILE2;
