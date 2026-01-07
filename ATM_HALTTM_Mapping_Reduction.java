import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class ATM_HALTTM_Mapping_Reduction 
{
    public static void main(String[] args)
    {
        String userInput;
        boolean correctType;
        String falseInputOutput = "q0,qa,qr:0,1:0,1,#:q0,0>q0,#,r;q0,1>q0,#,r;q0,#>q0,#,r:q0:qa:qr-#";

        // Obtain user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter input:");
        userInput = scanner.nextLine();
        scanner.close();

        TM userTM = new TM();
        correctType = userTM.parseInput(userInput);
        if (!correctType)
        {
            System.out.println(falseInputOutput);
        }
        else
        {
            TM modifiedTM = new TM();
            modifiedTM.copy(userTM);
            modifiedTM.createOutputTM();
            System.out.println(modifiedTM.print());
        }
    }
}

class TM 
{
    // Variables
    String turingMachineInput;
    List<String> states;
    List<String> inputAlphabet;
    List<String> tapeAlphabet;
    HashMap<String, String> transitionFunction;
    String startState;
    String acceptState;
    String rejectState;

    TM()
    {
        turingMachineInput = "";
        states = new ArrayList<String>();
        inputAlphabet = new ArrayList<String>();
        tapeAlphabet = new ArrayList<String>();
        transitionFunction =new HashMap<String, String>();
        startState = "";
        acceptState = "";
        rejectState = "";
    };

    // Type checks the user's Turing Machine input and builds the corresponding Turing Machine.
    // If the input is not a Turing Machine, return false. Otherwise, return true.
    boolean parseInput(String userInput)
    {
        // Separate the Turing Machine from the string
        String[] firstSplit = userInput.split("-");
        if (firstSplit.length != 2)
        {
            return false;
        }
        turingMachineInput = firstSplit[1];

        // Separate the Turing Machine components
        String[] secondSplit = firstSplit[0].split(":");
        if (secondSplit.length != 7)
        {
            return false;
        }
        
        // Separate the states
        states = Arrays.asList(secondSplit[0].split(","));
        if (states.size() < 2)
        {
            return false;
        }
        if (states.contains("qloop"))
        {
            return false;
        }
        Set<String> dupeTest = new HashSet<String>(states);
        if (dupeTest.size() < states.size())
        {
            return false;
        }

        // Separate the input alphabet
        inputAlphabet = Arrays.asList(secondSplit[1].split(","));
        if (inputAlphabet.size() == 1 && inputAlphabet.get(0).equals(""))
        {
            return false;
        }
        if (inputAlphabet.contains("#"))
        {
            return false;
        }
        dupeTest = new HashSet<String>(inputAlphabet);
        if (dupeTest.size() < inputAlphabet.size())
        {
            return false;
        }

        // Separate the tape alphabet
        tapeAlphabet = Arrays.asList(secondSplit[2].split(","));
        if (!tapeAlphabet.contains("#"))
        {
            return false;
        }
        dupeTest = new HashSet<String>(tapeAlphabet);
        if (dupeTest.size() < tapeAlphabet.size())
        {
            return false;
        }
        for (String letter : inputAlphabet)
        {
            if (!tapeAlphabet.contains(letter))
            {
                return false;
            }
        }

        // Separate the start state
        startState = secondSplit[4];
        if (!states.contains(startState))
        {
            return false;
        }

        // Separate the accept state
        acceptState = secondSplit[5];
        if (!states.contains(acceptState))
        {
            return false;
        }

        // Separate the reject state
        rejectState = secondSplit[6];
        if (rejectState.equals(acceptState) || !states.contains(rejectState))
        {
            return false;
        }

        // Separate the transition function
        String[] providedTransitions = secondSplit[3].split(";");
        if (providedTransitions.length > (states.size()*tapeAlphabet.size()))
        {
            return false;
        }
        for (String s : providedTransitions)
        {
            String[] transitionParts = s.split(">");
            if (transitionParts.length != 2)
            {
                return false;
            }
            // Check that transition contains allowed strings
            String[] transitionPart1 = transitionParts[0].split(",");
            if (transitionPart1.length != 2)
            {
                return false;
            }
            if (!states.contains(transitionPart1[0]))
            {
                return false;
            }
            if (!tapeAlphabet.contains(transitionPart1[1]))
            {
                return false;
            }
            String[] transitionPart2 = transitionParts[1].split(",");
            if (transitionPart2.length != 3)
            {
                return false;
            }
            if (!states.contains(transitionPart2[0]))
            {
                return false;
            }
            if (!tapeAlphabet.contains(transitionPart2[1]))
            {
                return false;
            }
            if (!(transitionPart2[2].equals("r") || transitionPart2[2].equals("l")))
            {
                return false;
            }
            // If passes all checks, add transition to transition function
            transitionFunction.put(transitionParts[0], transitionParts[1]);
        }
        // Go through all possible transitions. If a transition wasn't provided, add conventional transition to the reject state.
        for (String state : states)
        {
            for (String tapeLetter : tapeAlphabet)
            {
                if (transitionFunction.get(state + "," + tapeLetter) == null)
                {
                    transitionFunction.put(state + "," + tapeLetter, rejectState + ",#,r");
                }
            }
        }
        
        // Check that string (w) is made up of characters from the input alpabet
        if (!turingMachineInput.equals("#"))
        {
            for (char ch : turingMachineInput.toCharArray())
            {
                if (!inputAlphabet.contains(String.valueOf(ch)))
                {
                    return false;
                }
            }
        }
        return true;
    }

    // Create a copy of the input Turing Machine
    void copy(TM original)
    {
        this.turingMachineInput = original.turingMachineInput;
        for (String s : original.states)
        {
            this.states.add(s);
        }
        for (String s : original.inputAlphabet)
        {
            this.inputAlphabet.add(s);
        }
        for (String s : original.tapeAlphabet)
        {
            this.tapeAlphabet.add(s);
        }
        for (String s : original.transitionFunction.keySet())
        {
            this.transitionFunction.put(s, original.transitionFunction.get(s));
        }
        this.startState = original.startState;
        this.acceptState = original.acceptState;
        this.rejectState = original.rejectState;
    }

    // Create output Turing Machine
    void createOutputTM()
    {
        states.add("qloop");
        for (String state : states)
        {
            if (!state.equals("qloop"))
            {
                for (String tapeLetter : tapeAlphabet)
                {
                    String transitionResult = transitionFunction.get(state + "," + tapeLetter);
                    String[] transitionResultArray = transitionResult.split(",");
                    if (transitionResultArray[0].equals(rejectState))
                    {
                        transitionResult = "qloop," + transitionResultArray[1] + "," + transitionResultArray[2];
                    }
                    transitionFunction.put(state + "," + tapeLetter, transitionResult);
                }
            }
        }
        for (String letter : tapeAlphabet)
        {
            transitionFunction.put("qloop," + letter, "qloop,#,r");
        }
    }

    // Prints out the Turing Machine and associated string in the proper format
    String print()
    {
        String TM = "";
        for (String s : this.states)
        {
            TM = TM + s + ",";
        }
        TM = TM.substring(0, TM.length() - 1);
        TM = TM + ":";
        for (String s : this.inputAlphabet)
        {
            TM = TM + s + ",";
        }
        TM = TM.substring(0, TM.length() - 1);
        TM = TM + ":";
        for (String s : this.tapeAlphabet)
        {
            TM = TM + s + ",";
        }
        TM = TM.substring(0, TM.length() - 1);
        TM = TM + ":";
        for (String s : transitionFunction.keySet())
        {
            TM = TM + s + ">" + transitionFunction.get(s) + ";";
        }
        TM = TM.substring(0, TM.length() - 1);
        TM = TM + ":";
        TM = TM + startState + ":" + acceptState + ":" + rejectState + "-" + turingMachineInput;
        return TM;
    }
}
