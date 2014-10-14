#!/usr/bin/perl
open OUTFILE, ">conflict_free.valid";
open OUTFILE2, ">conflict_free.valid.dict";

# Were not required to check ship lengths, but not taking off if students did.
my @lengths = (5, 4, 3, 3, 2);

sub set_string {
    my $ret = "conflict_free(set(" . join(", ", @_) . ")).\n";
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
    #die if $length == 0;
    my $orientation = shift @arglist;
    my $x = shift @arglist;
    my $y = shift @arglist;
    return "ship($length, $orientation, $x, $y)";
}

sub horizontal_horizontal_different_rows {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "horizontal", 2, 7);
    push @ret, ship($length2, "horizontal", 2, 8);
    return @ret;
}

sub horizontal_horizontal_same_row {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "horizontal", 2, 7);
    push @ret, ship($length2, "horizontal", 2 + $length1, 7);
    return @ret;
}

sub vertical_vertical_different_columns {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 7, 2);
    push @ret, ship($length2, "vertical", 8, 2);
    return @ret;
}

sub vertical_vertical_same_column {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 7, 2);
    push @ret, ship($length2, "vertical", 7, 2 + $length1);
    return @ret;
}

sub vertical_horizontal_4_1 {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 2, 6);
    push @ret, ship($length2, "horizontal", 3, 7);
    return @ret;
}

sub vertical_horizontal_1_2 {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 6, 2);
    push @ret, ship($length2, "horizontal", 5, 2 + $length1);
    return @ret;
}

sub vertical_horizontal_2_3 {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 9, 4);
    push @ret, ship($length2, "horizontal", 9 - $length2, 5);
    return @ret;
}

sub vertical_horizontal_3_4 {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 5, 5);
    push @ret, ship($length2, "horizontal", 4, 4);
    return @ret;
}

sub vertical_horizontal_1_hc {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 6, 2);
    push @ret, ship($length2, "horizontal", 6, 2 + $length1);
    return @ret;
}

sub vertical_horizontal_2_hc {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 10, 2);
    push @ret, ship($length2, "horizontal", 10 - $length2 + 1, 2 + $length1);
    return @ret;
}

sub vertical_horizontal_3_hc {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 10, 6);
    push @ret, ship($length2, "horizontal", 10 - $length2 + 1, 5);
    return @ret;
}

sub vertical_horizontal_4_hc {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 6, 6);
    push @ret, ship($length2, "horizontal", 6, 5);
    return @ret;
}

sub vertical_horizontal_1_vc {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 5, 3);
    push @ret, ship($length2, "horizontal", 6, 2 + $length1);
    return @ret;
}

sub vertical_horizontal_2_vc {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 10, 6);
    push @ret, ship($length2, "horizontal", 10 - $length2, 5 + $length1);
    return @ret;
}

sub vertical_horizontal_3_vc {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 10, 6);
    push @ret, ship($length2, "horizontal", 10 - $length2, 6);
    return @ret;
}

sub vertical_horizontal_4_vc {
    my @arglist = @_;
    my $length1 = shift @arglist;
    my $length2 = shift @arglist;
    my @ret = ();
    push @ret, ship($length1, "vertical", 5, 6);
    push @ret, ship($length2, "horizontal", 6, 6);
    return @ret;
}

sub ship_list {
    my @ships = (ship(5, "horizontal", 1, 1),
                 ship(4, "horizontal", 1, 2),
                 ship(3, "horizontal", 1, 3),
                 ship(3, "horizontal", 1, 4),
                 ship(2, "horizontal", 1, 5));
    my @arglist = @_;
    my $index1 = shift;
    my $index2 = shift;
    $ships[$index1] = shift;
    $ships[$index2] = shift;
    return @ships;
}

#Basic corresponding rows/columns, vertical
@set = ();
for (my $i = 0; $i < @lengths; $i++) {
    my @tuple = ($lengths[$i], "vertical");
    push @set, ship(@tuple, $i+1, top(@tuple));
}
print OUTFILE set_string(@set);
print OUTFILE2 "Basic corresponding columns, vertical\n";

#Basic corresponding rows/columns, horizontal
@set = ();
for (my $i = 0; $i < @lengths; $i++) {
    my @tuple = ($lengths[$i], "horizontal");
    push @set, ship(@tuple, left(@tuple), $i + 1);
}
print OUTFILE set_string(@set);
print OUTFILE2 "Basic corresponding rows, horizontal\n";

for (my $i = 0; $i < @lengths; $i++) {
    for (my $j = $i + 1; $j < @lengths; $j++) {
        print OUTFILE set_string(ship_list($i, $j,
                                 horizontal_horizontal_different_rows(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "horizontal_horizontal_different_rows($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 horizontal_horizontal_different_rows(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "horizontal_horizontal_different_rows($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 horizontal_horizontal_same_row(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "horizontal_horizontal_same_row($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 horizontal_horizontal_same_row(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "horizontal_horizontal_same_row($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_vertical_different_columns(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_vertical_different_columns($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_vertical_different_columns(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_vertical_different_columns($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_vertical_same_column(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_vertical_same_column($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_vertical_same_column(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_vertical_same_column($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_horizontal_4_1(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_horizontal_4_1($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_horizontal_4_1(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_horizontal_4_1($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_horizontal_1_2(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_horizontal_1_2($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_horizontal_1_2(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_horizontal_1_2($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_horizontal_2_3(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_horizontal_2_3($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_horizontal_2_3(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_horizontal_2_3($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_horizontal_3_4(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_horizontal_3_4($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_horizontal_3_4(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_horizontal_3_4($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_horizontal_1_hc(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_horizontal_1_hc($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_horizontal_1_hc(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_horizontal_1_hc($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_horizontal_2_hc(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_horizontal_2_hc($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_horizontal_2_hc(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_horizontal_2_hc($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_horizontal_3_hc(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_horizontal_3_hc($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_horizontal_3_hc(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_horizontal_3_hc($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_horizontal_4_hc(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_horizontal_4_hc($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_horizontal_4_hc(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_horizontal_4_hc($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_horizontal_1_vc(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_horizontal_1_vc($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_horizontal_1_vc(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_horizontal_1_vc($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_horizontal_2_vc(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_horizontal_2_vc($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_horizontal_2_vc(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_horizontal_2_vc($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_horizontal_3_vc(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_horizontal_3_vc($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_horizontal_3_vc(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_horizontal_3_vc($j, $i)\n";
        print OUTFILE set_string(ship_list($i, $j,
                                 vertical_horizontal_4_vc(
                                 $lengths[$i], $lengths[$j])));
        print OUTFILE2 "vertical_horizontal_4_vc($i, $j)\n";
        print OUTFILE set_string(ship_list($j, $i,
                                 vertical_horizontal_4_vc(
                                 $lengths[$j], $lengths[$i])));
        print OUTFILE2 "vertical_horizontal_4_vc($j, $i)\n";
    }
}

close OUTFILE;
close OUTFILE2;
