package ru.amalnev.jnms.web.undo;

import java.io.Serializable;
import java.util.Stack;

public class UndoOperationsStack extends Stack<UndoOperation> implements Serializable
{
}
