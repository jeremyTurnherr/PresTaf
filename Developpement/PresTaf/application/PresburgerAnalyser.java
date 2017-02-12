//
//  PresburgerAnalyser.java
//  PresTafProject
//
//  Created by couvreur on Tue Dec 02 2003.
//

package application;

import prestaf.*;
import java.io.Reader;
import java.io.StringReader;

public class PresburgerAnalyser {

    public Presburger formula;
    
    public PresburgerAnalyser(Reader file, boolean verbose)
    {
        PresburgerMonteurYacc yyparser;
        try {
            yyparser = new PresburgerMonteurYacc(file,verbose);
            formula = yyparser.parse();
        }
        catch(Throwable e) {
            System.out.println(e.toString());
            formula = null;
        }
    }
    
    public PresburgerAnalyser(String s, boolean verbose)
    {
        PresburgerMonteurYacc yyparser;
        try {
            yyparser = new PresburgerMonteurYacc(new StringReader(s),verbose);
            formula = yyparser.parse();
        }
        catch(Throwable e) {
            System.out.println(e.toString());
            formula = null;
        }
    }
}
