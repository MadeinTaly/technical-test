# Technical test

My solutions for the two exercises. Java 25 with Maven, JUnit 6 for the tests, nothing else at runtime.

```
mvn verify
```

## 1. Daily report per IP address

```
java -jar target/technical-test-1.0.0.jar [--format=csv|json] [--log-file=<path>] [--report-dir=<path>]
```

Without arguments it reads `/logfiles/requests.log` and writes `/reports/ipaddr.csv`, as asked.
With `--format=json` it writes `/reports/ipaddr.json` instead.

A few things worth saying:

- only the lines with STATUS exactly `OK` are counted. Anything that doesn't parse (wrong field count,
  bytes not a number, negative bytes, blank address) is skipped without complaining.
- rows are sorted by number of requests (desc), then bytes (desc), then IP address, so running twice on
  the same file gives the same output.
- percentages are BigDecimal with two decimals, HALF_UP.
- the report goes to a temp file first and then gets moved in place, so nobody can read a half written file.
- a new output format is one ReportFormatter plus one constant in OutputFormat.

The code is in `traffic.log` (parsing), `traffic.report` (aggregation, percentages, ordering),
`traffic.format` (csv and json) and `traffic` (command line and orchestration).

## 2. Multiplication by addition

`ArrayInteger` keeps a sign and an `int[]` of digits, least significant first, same layout as the exercise
(`15` becomes `[5, 1]`).

The `*` operator is never used. For every digit `d` of the shorter number, at position `i`, the other number
is added `d` times to the result, shifted by `i` positions. Each of those additions goes through `DigitSum`,
which accepts two digits in [0, 9] plus the carry and gives back the resulting digit and the next carry.

```java
ArrayInteger.of("15").multiply(ArrayInteger.of("2")).digits();  // [0, 3]
Factorial.of(100);                                              // 158 digits
```

Tests check the results against `BigInteger`, with 500 random operands and factorials up to 150.
