package proguard.normalize;

import proguard.AppView;
import proguard.classfile.visitor.ParallelAllClassVisitor;
import proguard.pass.Pass;

/**
 * Ensures all strings are at most 65535 bytes in length, when encoded as modified UTF-8.
 *
 * @see LargeStringSplitter
 */
public class StringNormalizer implements Pass {


  @Override
  public void execute(AppView appView) throws Exception {
    appView.programClassPool.accept(
        new ParallelAllClassVisitor(
            () -> new LargeStringSplitter(appView.programClassPool, appView.libraryClassPool)));
  }
}
