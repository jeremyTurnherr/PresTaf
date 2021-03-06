//#############################################
//## file: PresburgerMonteurYacc.java
//## Generated by Byacc/j
//#############################################
/**
 * BYACC/J Semantic Value for parser: PresburgerMonteurYacc
 * This class provides some of the functionality
 * of the yacc/C 'union' directive
 */
 package application;
 
public class PresburgerMonteurYaccVal
{
/**
 * integer value of this 'union'
 */
public int ival;

/**
 * double value of this 'union'
 */
public double dval;

/**
 * string value of this 'union'
 */
public String sval;

/**
 * object value of this 'union'
 */
public Object obj;

//#############################################
//## C O N S T R U C T O R S
//#############################################
/**
 * Initialize me as an int
 */
public PresburgerMonteurYaccVal(int val)
{
  ival=val;
}

/**
 * Initialize me as a double
 */
public PresburgerMonteurYaccVal(double val)
{
  dval=val;
}

/**
 * Initialize me as a string
 */
public PresburgerMonteurYaccVal(String val)
{
  sval=val;
}

/**
 * Initialize me as an Object
 */
public PresburgerMonteurYaccVal(Object val)
{
  obj=val;
}
}//end class

//#############################################
//## E N D    O F    F I L E
//#############################################
