import java.util.Scanner;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class ADFA_Decidability {
    public static void main (String[] args)
    {
        // Variables
        String userInput;
        boolean correctFormat;

        // Obtain user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter input:");
        userInput = scanner.nextLine();
        scanner.close();

        DFA userDFA = new DFA();
        correctFormat = userDFA.parseInput(userInput);
        if (correctFormat)
        {
            System.out.println(userDFA.simulate());
        }
        else
        {
            System.out.println(correctFormat);
        }
    }
}

class DFA 
{
    // Variables
    List<String> states;
    List<String> alphabet;
    HashMap<String, String> transitionFunction;
    String startState;
    List<String> acceptStates;
    String input;

    // Constructor
    DFA()
    {
        states = new ArrayList<String>();
        alphabet = new ArrayList<String>();
        transitionFunction = new HashMap<String, String>();
        startState = "";
        acceptStates = new ArrayList<String>();
        input = "";
    }

    // This method takes a string in the format <B,w> as a parameter.
    // It parses the string into the relevant components and stores the data in the corresponding variables.
    // It is also a type check. If the string parameter is not in the specified <B,w> format, it will return false
    // and the data will not be stored. Otherwise, returns true.
    boolean parseInput(String userInput)
    {
        // Separate the DFA from the string
        String[] firstSplit = userInput.split("-");
        if (firstSplit.length != 2)
        {
            return false;
        }
        input = firstSplit[1];
        
        // Separate the DFA components
        String[] secondSplit = firstSplit[0].split(":");
        if (secondSplit.length != 5)
        {
            this.clearDFA();
            return false;
        }
        
        // Separate the states
        states = Arrays.asList(secondSplit[0].split(","));
        if (states.size() == 1 && states.get(0).equals(""))
        {
            this.clearDFA();
            return false;
        }
        Set<String> dupeTest = new HashSet<String>(states);
        if (dupeTest.size() < states.size())
        {
            this.clearDFA();
            return false;
        }

        // Separate the alphabet
        alphabet = Arrays.asList(secondSplit[1].split(","));
        if (alphabet.size() == 1 && alphabet.get(0).equals(""))
        {
            this.clearDFA();
            return false;
        }
        dupeTest = new HashSet<String>(alphabet);
        if (dupeTest.size() < alphabet.size())
        {
            this.clearDFA();
            return false;
        }
        
        // Separate the start state
        startState = secondSplit[3];
        if (!states.contains(startState))
        {
            this.clearDFA();
            return false;
        }

        // Separate the accept states
        acceptStates = Arrays.asList(secondSplit[4].split(","));
        if (acceptStates.size() == 1 && acceptStates.get(0).equals(""))
        {
            this.clearDFA();
            return false;
        }
        dupeTest = new HashSet<String>(acceptStates);
        if (dupeTest.size() < acceptStates.size())
        {
            this.clearDFA();
            return false;
        }
        for (String state : acceptStates)
        {
            if (!states.contains(state))
            {
                this.clearDFA();
                return false;
            }
        }

        // Separate the transition function
        String[] providedTransitions = secondSplit[2].split(";");
        for (String s : providedTransitions)
        {
            String[] transitionParts = s.split(">");
            if (transitionParts.length != 2)
            {
                this.clearDFA();
                return false;
            }
            // Check that transition contains allowed strings
            String[] transitionPart1 = transitionParts[0].split(",");
            if (transitionPart1.length != 2)
            {
                this.clearDFA();
                return false;
            }
            if (!states.contains(transitionPart1[0]))
            {
                this.clearDFA();
                return false;
            }
            if (!alphabet.contains(transitionPart1[1]))
            {
                this.clearDFA();
                return false;
            }
            if (!states.contains(transitionParts[1]))
            {
                this.clearDFA();
                return false;
            }
            // If passes all checks, add transition to transition function
            transitionFunction.put(transitionParts[0], transitionParts[1]);
        }
        if (transitionFunction.size() != states.size()*alphabet.size())
        {
            this.clearDFA();
            return false;
        }

        // Check that string (w) is made up of characters from the alpabet
        if (!input.equals("#"))
        {
            for (char ch : input.toCharArray())
            {
                if (!alphabet.contains(String.valueOf(ch)))
                {
                    this.clearDFA();
                    return false;
                }
            }
        }

        return true;
    }

    // Simulate B on w. If simulation ends in accept state of B, return true. Otherwise, return false.
    boolean simulate()
    {
        String currentState = this.startState;

        // If object is empty, return false
        if (this.states.isEmpty() || this.alphabet.isEmpty() || this.transitionFunction.isEmpty() || this.startState.equals("") || this.acceptStates.isEmpty() || this.input.equals(""))
        {
            return false;
        }
        // If input is the empty string
        if (input.equals("#"))
        {
            if (acceptStates.contains(currentState))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        for (char ch : input.toCharArray())
        {
            String nextState = transitionFunction.get(currentState + "," + String.valueOf(ch));
            currentState = nextState;
        }
        if (acceptStates.contains(currentState))
        {
            return true;
        }
        return false;
    }

    // Sets all variables back to default.
    void clearDFA()
    {
        states = new ArrayList<String>();
        alphabet = new ArrayList<String>();
        transitionFunction = new HashMap<String, String>();
        startState = "";
        acceptStates = new ArrayList<String>();
        input = "";
    }
}
