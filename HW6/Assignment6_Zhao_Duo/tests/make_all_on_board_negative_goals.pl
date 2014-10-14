#!/usr/bin/perl
open OUTFILE, ">all_on_board.invalid";
open OUTFILE2, ">all_on_board.invalid.dict";

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

for (my $special = 0; $special < @lengths; $special++) {
    #All in left corner, vertical
    my @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "vertical");
        if ($i == $special) {
            push @set, ship(@tuple, left(@tuple) - 1, top(@tuple))
        }
        else {
            push @set, ship(@tuple, left(@tuple), top(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "All in left corner, vertical, ship $special too far left\n";
}

for (my $special = 0; $special < @lengths; $special++) {
    #All in left corner, vertical
    my @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "vertical");
        if ($i == $special) {
            push @set, ship(@tuple, left(@tuple), top(@tuple) - 1)
        }
        else {
            push @set, ship(@tuple, left(@tuple), top(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "All in left corner, vertical, ship $special too high\n";
}

for (my $special = 0; $special < @lengths; $special++) {
    #All in left corner, horizontal
    my @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "horizontal");
        if ($i == $special) {
            push @set, ship(@tuple, left(@tuple) - 1, top(@tuple))
        }
        else {
            push @set, ship(@tuple, left(@tuple), top(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "All in left corner, horizontal, ship $special too far left\n";
}

for (my $special = 0; $special < @lengths; $special++) {
    #All in left corner, horizontal
    my @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "horizontal");
        if ($i == $special) {
            push @set, ship(@tuple, left(@tuple), top(@tuple) - 1)
        }
        else {
            push @set, ship(@tuple, left(@tuple), top(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "All in left corner, horizontal, ship $special too high\n";
}

for (my $special = 0; $special < @lengths; $special++) {
#Corresponding rows/columns, vertical
    @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "vertical");
        if ($i == $special) {
            push @set, ship(@tuple, 0, top(@tuple));
        }
        else {
            push @set, ship(@tuple, $i+1, top(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "Corresponding columns, vertical, ship $special too far left\n";

}

for (my $special = 0; $special < @lengths; $special++) {
#Corresponding rows/columns, vertical
    @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "vertical");
        if ($i == $special) {
            push @set, ship(@tuple, $i+1, top(@tuple) - 1);
        }
        else {
            push @set, ship(@tuple, $i+1, top(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "Corresponding columns, vertical, ship $special too high\n";
}

for (my $special = 0; $special < @lengths; $special++) {
#Corresponding rows/columns, vertical
    @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "horizontal");
        if ($i == $special) {
            push @set, ship(@tuple, 0, top(@tuple));
        }
        else {
            push @set, ship(@tuple, $i+1, top(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "Corresponding rows, horizontal, ship $special too far left\n";
}

for (my $special = 0; $special < @lengths; $special++) {
#Corresponding rows/columns, horizontal
    @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "horizontal");
        if ($i == $special) {
            push @set, ship(@tuple, $i+1, top(@tuple) - 1);
        }
        else {
            push @set, ship(@tuple, $i+1, top(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "Corresponding rows, horizontal, ship $special too high\n";
}

# Bottom right corner, horizontal
for (my $special = 0; $special < @lengths; $special++) {
    @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "horizontal");
        if ($i == $special) {
            push @set, ship(@tuple, right(@tuple)+1, bottom(@tuple));
        }
        else {
            push @set, ship(@tuple, right(@tuple), bottom(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "Bottom right corner, horizontal, ship $special too far right\n";
}

for (my $special = 0; $special < @lengths; $special++) {
    @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "horizontal");
        if ($i == $special) {
            push @set, ship(@tuple, right(@tuple), bottom(@tuple) + 1);
        }
        else {
            push @set, ship(@tuple, right(@tuple), bottom(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "Bottom right corner, horizontal, ship $special too low\n";
}

# Bottom right corner, horizontal
for (my $special = 0; $special < @lengths; $special++) {
    @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "vertical");
        if ($i == $special) {
            push @set, ship(@tuple, right(@tuple)+1, bottom(@tuple));
        }
        else {
            push @set, ship(@tuple, right(@tuple), bottom(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "Bottom right corner, vertical, ship $special too far right\n";
}

for (my $special = 0; $special < @lengths; $special++) {
    @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "vertical");
        if ($i == $special) {
            push @set, ship(@tuple, right(@tuple), bottom(@tuple) + 1);
        }
        else {
            push @set, ship(@tuple, right(@tuple), bottom(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "Bottom right corner, horizontal, ship $special too low\n";
}

# Bottom right corner, separate rows, horizontal
for (my $special = 0; $special < @lengths; $special++) {
    @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "horizontal");
        if ($i == $special) {
            push @set, ship(@tuple, right(@tuple) + 1, 10 - $i);
        }
        else {
            push @set, ship(@tuple, right(@tuple), 10 - $i);
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "Bottom right corner, horizontal, separate rows, ship $special too far right\n";
}

for (my $special = 0; $special < @lengths; $special++) {
    @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "horizontal");
        if ($i == $special) {
            push @set, ship(@tuple, right(@tuple), 11);
        }
        else {
            push @set, ship(@tuple, right(@tuple), 10 - $i);
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "Bottom right corner, horizontal, separate rows, ship $special too low\n";
}

# Bottom right corner, separate columns, vertical
for (my $special = 0; $special < @lengths; $special++) {
    @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "vertical");
        if ($i == $special) {
            push @set, ship(@tuple, 11, bottom(@tuple));
        }
        else {
            push @set, ship(@tuple, 10 - $i, bottom(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "Bottom right corner, vertical, separate columns, ship $special too far right\n";
}

for (my $special = 0; $special < @lengths; $special++) {
    @set = ();
    for (my $i = 0; $i < @lengths; $i++) {
        my @tuple = ($lengths[$i], "vertical");
        if ($i == $special) {
            push @set, ship(@tuple, 10 - $i, bottom(@tuple) + 1);
        }
        else {
            push @set, ship(@tuple, 10 - $i, bottom(@tuple));
        }
    }
    print OUTFILE set_string(@set);
    print OUTFILE2 "Bottom right corner, vertical, separate columns, ship $special too low\n";
}

close OUTFILE;
close OUTFILE2;
