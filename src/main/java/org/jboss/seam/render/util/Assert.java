package org.jboss.seam.render.util;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class Assert
{
   public static void notNull(final Object object, final String message) throws IllegalStateException
   {
      if (object == null)
      {
         throw new IllegalStateException(message);
      }
   }

   public static void equals(final Object left, final Object right, final String message)
   {
      if (left == right)
         return;
      else if ((left == null) || (right == null))
      {
         throw new IllegalStateException(message);
      }
      else if (left.equals(right))
         return;
      else
      {
         throw new IllegalStateException(message);
      }

   }
}
