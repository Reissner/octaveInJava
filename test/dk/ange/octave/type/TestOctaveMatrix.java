package dk.ange.octave.type;

import java.io.OutputStreamWriter;
import java.io.StringWriter;

import junit.framework.Assert;
import junit.framework.TestCase;
import dk.ange.octave.Octave;

/**
 * @author Kim Hansen
 */
@SuppressWarnings("deprecation")
public class TestOctaveMatrix extends TestCase {

    /**
     * @throws Exception
     */
    public void testConstructor() throws Exception {
        OctaveMatrix matrix = new OctaveMatrix();
        Assert.assertEquals(0, matrix.rows());
        Assert.assertEquals(0, matrix.columns());
        Assert.assertEquals("" + //
                "# name: matrix\n" + //
                "# type: matrix\n" + //
                "# rows: 0\n" + //
                "# columns: 0\n" + //
                "\n" //
        , matrix.toText("matrix"));
    }

    /**
     * @throws Exception
     */
    public void testConstructorMatrix() throws Exception {
        double[] numbers = { 1, 2, 3, 4, 5, 6 };
        OctaveMatrix matrix = new OctaveMatrix(numbers, 2, 3);
        Assert.assertEquals(2, matrix.rows());
        Assert.assertEquals(3, matrix.columns());
        Assert.assertEquals("" + //
                "# name: mymatrix\n" + //
                "# type: matrix\n" + //
                "# rows: 2\n" + //
                "# columns: 3\n" + //
                " 1.0 3.0 5.0\n" + //
                " 2.0 4.0 6.0\n\n" //
        , matrix.toText("mymatrix"));
    }

    /**
     * @throws Exception
     */
    public void testConstructorIntInt() throws Exception {
        OctaveMatrix matrix = new OctaveMatrix(2, 3);
        Assert.assertEquals(2, matrix.rows());
        Assert.assertEquals(3, matrix.columns());
        Assert.assertEquals("" + //
                "# name: matrix\n" + //
                "# type: matrix\n" + //
                "# rows: 2\n" + //
                "# columns: 3\n" + //
                " 0.0 0.0 0.0\n" + //
                " 0.0 0.0 0.0\n\n" //
        , matrix.toText("matrix"));
        matrix.set(42, 1, 2);
        Assert.assertEquals("" + //
                "# name: myother\n" + //
                "# type: matrix\n" + //
                "# rows: 2\n" + //
                "# columns: 3\n" + //
                " 0.0 42.0 0.0\n" + //
                " 0.0 0.0 0.0\n\n" //
        , matrix.toText("myother"));
        matrix.set(2, 2, 1);
        Assert.assertEquals("" + //
                "# name: myother\n" + //
                "# type: matrix\n" + //
                "# rows: 2\n" + //
                "# columns: 3\n" + //
                " 0.0 42.0 0.0\n" + //
                " 2.0 0.0 0.0\n\n" //
        , matrix.toText("myother"));
        matrix.set(4.0, 2, 2);
        Assert.assertEquals("" + //
                "# name: myother\n" + //
                "# type: matrix\n" + //
                "# rows: 2\n" + //
                "# columns: 3\n" + //
                " 0.0 42.0 0.0\n" + //
                " 2.0 4.0 0.0\n\n" //
        , matrix.toText("myother"));
    }

    /**
     * @throws Exception
     */
    public void testGrowth() throws Exception {
        OctaveMatrix matrix;

        matrix = new OctaveMatrix(0, 0);
        Assert.assertEquals(0, matrix.rows());
        Assert.assertEquals(0, matrix.columns());
        Assert.assertEquals("" + //
                "# name: matrix\n" + //
                "# type: matrix\n" + //
                "# rows: 0\n" + //
                "# columns: 0\n\n" //
        , matrix.toText("matrix"));
        matrix.set(1, 1, 1);
        Assert.assertEquals(1, matrix.rows());
        Assert.assertEquals(1, matrix.columns());
        Assert.assertEquals("" + //
                "# name: matrix\n" + //
                "# type: matrix\n" + //
                "# rows: 1\n" + //
                "# columns: 1\n" + //
                " 1.0\n\n" //
        , matrix.toText("matrix"));
        matrix.set(3, 3, 1);
        Assert.assertEquals(3, matrix.rows());
        Assert.assertEquals(1, matrix.columns());
        Assert.assertEquals("" + //
                "# name: matrix\n" + //
                "# type: matrix\n" + //
                "# rows: 3\n" + //
                "# columns: 1\n" + //
                " 1.0\n" + //
                " 0.0\n" + //
                " 3.0\n\n" //
        , matrix.toText("matrix"));

        matrix = new OctaveMatrix(0, 0);
        matrix.set(3.0, 1, 3);
        Assert.assertEquals("" + //
                "# name: matrix\n" + //
                "# type: matrix\n" + //
                "# rows: 1\n" + //
                "# columns: 3\n" + //
                " 0.0 0.0 3.0\n" + //
                "\n" //
        , matrix.toText("matrix"));
    }

    /**
     * @throws Exception
     */
    public void testOctaveGet() throws Exception {
        Octave octave = new Octave();
        octave.execute("m=[1 2;3 4];");
        OctaveNdMatrix matrix = new OctaveMatrix(octave.get("m"));
        matrix = new OctaveMatrix(octave.get("m"));
        Assert.assertEquals("" + //
                "# name: m\n" + //
                "# type: matrix\n" + //
                "# rows: 2\n" + //
                "# columns: 2\n" + //
                " 1.0 2.0\n" + //
                " 3.0 4.0\n" + //
                "\n" //
        , matrix.toText("m"));
    }

    /**
     * @throws Exception
     */
    public void testOctaveSetExecGet() throws Exception {
        double[] numbers = { 1, 2, 3, 4, 5, 6 };
        Octave octave = new Octave();
        OctaveNdMatrix in = new OctaveMatrix(numbers, 2, 3);
        octave.set("in", in);
        octave.execute("out=in;");
        OctaveNdMatrix out = new OctaveMatrix(octave.get("out"));
        Assert.assertEquals(in.toString(), out.toString());
        octave.execute("slicerow=in(2,:); slicecol=in(:,2);");
        OctaveMatrix slicerow = new OctaveMatrix(octave.get("slicerow"));
        OctaveMatrix slicecol = new OctaveMatrix(octave.get("slicecol"));
        assertEquals(2.0, slicerow.get(1, 1));
        assertEquals(4.0, slicerow.get(1, 2));
        assertEquals(6.0, slicerow.get(1, 3));
        assertEquals(3.0, slicecol.get(1, 1));
        assertEquals(4.0, slicecol.get(2, 1));

    }

    /**
     * @throws Exception
     */
    public void testPerformance() throws Exception {
        OctaveMatrix matrix = new OctaveMatrix(30, 0);
        long t = System.currentTimeMillis();
        // 4125 was the number of containers in a real job.
        for (int pos = 1; pos <= 4125; ++pos) {
            matrix.set(4.2, 1, pos);
            matrix.set(4.2, 30, pos);
        }
        long timeused = System.currentTimeMillis() - t;
        if (timeused > 500) {
            fail("Performance test didn't finish in 500ms (used " + timeused + "ms)");
        }

        matrix = new OctaveMatrix(0, 30);
        t = System.currentTimeMillis();
        // 700 is just some random number
        for (int pos = 1; pos <= 700; ++pos) {
            matrix.set(4.2, pos, 1);
            matrix.set(4.2, pos, 30);
        }
        timeused = System.currentTimeMillis() - t;
        if (timeused > 500) {
            fail("Performance test didn't finish in 500ms (used " + timeused + "ms)");
        }
    }

    /**
     * Test how the system handles save of Inf and NaN
     * 
     * @throws Exception
     */
    public void testSaveNanInf() throws Exception {
        StringWriter stderr = new StringWriter();
        Octave octave = new Octave(null, new OutputStreamWriter(System.out), stderr);
        octave.execute("ok=1;");
        octave.execute("xnan=[NaN 0];");
        new OctaveScalar(octave.get("ok"));
        OctaveMatrix xnan = new OctaveMatrix(octave.get("xnan"));
        assertEquals(Double.NaN, xnan.get(1, 1));
        assertEquals(Double.valueOf(0), xnan.get(1, 2));
        new OctaveScalar(octave.get("ok"));
        octave.execute("xinf=[Inf -Inf];");
        new OctaveScalar(octave.get("ok"));
        OctaveMatrix xinf = new OctaveMatrix(octave.get("xinf"));
        assertEquals(Double.POSITIVE_INFINITY, xinf.get(1, 1));
        assertEquals(Double.NEGATIVE_INFINITY, xinf.get(1, 2));
        new OctaveScalar(octave.get("ok"));
        octave.close();
        stderr.close();
        assertEquals("", stderr.toString()); // No warning when saving matrix with NaN/Inf
    }

}
