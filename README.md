# BProC CLI - Basic Processors Compiler Command Line Interface #
## Description ##
BProC is a compiler for basic processor designs, it uses assembly code written in `.baspm` 
files. BProC-CLI is a simple command line interface for compiling assembler files `.bpasm` 
for basic processors intended for education use. The vision of the project is to provide 
simple way to generate RAM programs while designing basic processors.
The project, as it is, provide a rigid instruction set, but the goal is to provide a 
configurable instruction set.

## Installation ##
Choose one of the options available:
- Pakage install
- JAR run
### Package install ###
1. Download one of the packages suitable for your device in the release 
section : <a href="https://github.com/jugubell/bproc-cli/releases">Release section</a>
2. Install the package with dnf/apt or execute installer on Windows.
   - Supported packages: rpm, deb, exe.
   - Supported architectures: x86_64, aarch64

### JAR run ###
#### Prerequisites ####
Java 8 or later runtime is needed to run the compiler. To verify, 
in the terminal run `java -version` or `java --version`. 
Otherwise, install the JRE for your device from <a href="https://adoptium.net/en-GB/temurin/releases?version=21">here</a>.

#### Run the .jar file ####
The `.jar` file provided can be used to compile `.bpasm` assembly files in a terminal:
1. Download the `.jar` file from the <a href="https://github.com/jugubell/bproc-cli/releases">Release section</a> 
2. Run with: `java -jar bproc-cli.jar <argument(s)>`

## Usage ##
Here is how to use the BProC-CLI:
```
bproc [-action <inputFile (*.bpasm)>] [optional -output <outputFileName/outputDirectory>] [optional --option <compileType>]
```
If you're using the JAR file:
```
java -jar bproc-cli.jar [-action <inputFile (*.bpasm)>] [optional -output <outputFileName/outputDirectory>] [optional --option <compileType>]
```
#### Arguments ####
- inputFile (*.bpasm): A valid assembler file absolute or relative path with `.bpasm` extension.
- outputFileName/outputDirectory: A valid output directory, if the name file 
is not provided, it will be generated.
- compileType [binary|hexv3]: Type of compilation : default 'binary'

#### Actions ####
- `-s` : verify syntax.
- `-g` : verify syntax, compile then generate the compiled code.
- `-o` : verify syntax, compile and save the compiled code to a file

#### Options ####
- `--bin` : compiles to binary [value by default]
- `--hex` : compiles to hexadecimal
- `--hexv3` : compiles to hexadecimal version 3 format (compatible for Logisim RAM)
- `--vhdl` : compiles to a portion of VHDL RAM initialization signal
- `--vrlg` : compiles to a portion of Verilog RAM initial bloc

#### Other arguments ####
- `--help`, `-h`, `help`       : shows the help.
- `--version`, `-v`, `version` : shows the version.
- `--instruction-set`, `--is`  : shows the supported instruction set.

## Supported Instruction Set ##
This table recapitulate the current supported instruction set.

| Instruction | Op code | Type     | Needs an operand | Description                                                                  |
|-------------|---------|----------|------------------|------------------------------------------------------------------------------|
| ***AND***   | 0x0000  | Hardware | Yes              | Logic *'AND'* between operand and accumulator A                              | 
| ***ADD***   | 0x1000  | Hardware | Yes              | Addition of the address' value of the operand and value of A                 |
| ***LDA***   | 0x2000  | Hardware | Yes              | Load address' value of the operand into A                                    |
| ***STA***   | 0x3000  | Hardware | Yes              | Store the value of A into the address of the operand                         |
| ***BIN***   | 0x4000  | Hardware | Yes              | Unconditional branch of program counter to the address indicated             |
| ***BSA***   | 0x5000  | Hardware | Yes              | Subroutine branch stored in address $x + 1                                   |
| ***ISZ***   | 0x6000  | Hardware | Yes              | Increment content of address (operand) and go to the next instr. if result=0 |
| ***CLA***   | 0x7800  | Hardware | No               | Clear accumulator A                                                          |
| ***CLE***   | 0x7400  | Hardware | No               | Clear flag E (carry out)                                                     |
| ***LNA***   | 0x7200  | Hardware | No               | Logic `NOT` of accumulator A (A = Ā)                                         |
| ***LNE***   | 0x7100  | Hardware | No               | Logic `NOT` of flag E (E = Ē)                                                |
| ***SRA***   | 0x7080  | Hardware | No               | Shift right of the accumulator A through the flag E                          |
| ***SLA***   | 0x7040  | Hardware | No               | Shift left of the accumulator A through the flag E                           |
| ***INC***   | 0x7020  | Hardware | No               | Increment accumulator A                                                      |
| ***SPA***   | 0x7010  | Hardware | No               | Skip the next instr. if accumulator A > 0                                    |
| ***SNA***   | 0x7008  | Hardware | No               | Skip the next instr. if accumulator A < 0                                    |
| ***SZA***   | 0x7004  | Hardware | No               | Skip the next instr. if accumulator A = 0                                    |
| ***SZE***   | 0x7002  | Hardware | No               | Skip the next instr. if flag E = 0                                           |
| ***HLT***   | 0x7001  | Hardware | No               | Halt the program until hardware reset                                        |
| ***RIR***   | 0xF800  | Hardware | No               | Read input register (GPIO in)                                                |
| ***WOR***   | 0xF400  | Hardware | No               | Write output register (GPIO out)                                             |
| ***SFI***   | 0xF200  | Hardware | No               | Skip the next instr. if flag FGI = 1 (Input reading flag)                    |
| ***SFO***   | 0xF100  | Hardware | No               | Skip the next instr. if flag FGO = 0 (Output writing flag)                   |
| ***LDD***   | 0xF080  | Software | No               | Load data in immediate mode                                                  |
| ***JMP***   | 0x0000  | Software | No               | Jump to label in assembly code                                               |

## Keywords ##
There are 2 keywords :
  - `.data`     : for data declaration in RAM
  - `start:`    : label to indicate the program start. This label is put after data declaration
  - `0x` prefix or `h` suffix : for addresses and values in hexadecimal

## TODO ##
1. Integrate an SQLite database to manage a custom instruction set and configurations.
2. Extend RAM use to custom RAM capacity.
3. Enable software instruction from one or more hardware instruction.

## Credit ##
Default instruction set mostly inspired from PhD lab work at University of Blida.
