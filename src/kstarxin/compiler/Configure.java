package kstarxin.compiler;

public class Configure {
    public final static int         PTR_SIZE                = 8;
    public final static int         CALL_STACK_ALIGN        = 16;
    public final static int         INLINE_THRESHOULD       = 50;
    public final static boolean     PRINT_AST_SYMBOLTABLE   = false;
    public final static int         CHAR_SIZE               = 1;
    public final static boolean     PRINT_LIVEINOUT         = false;
    public final static boolean     PRINT_REG_ALLOC_LIVEOUT = false;
    public final static short       INLINE_LEVEL            = 4;
    public final static short       RECURSIVE_INLINE_LEVEL  = 1;
}
