package org.codehaus.mojo.versions.ordering;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.junit.Ignore;
import org.junit.Test;

import java.util.StringTokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MyTest {

    @Test
    public void testDashRedhat() {
        assertEquals(0, compare("2.2.0.redhat-1", "2.2.0-redhat-1"));
    }

    @Test
    public void testRedhatBuildWithZeros() {
        assertEquals(-1, compare("2.2.0-redhat-1", "2.2.0-redhat-00002"));
    }

    @Test
    @Ignore("shouldn't happen")
    public void testRedhatBuildWithZerosOld() {
        assertEquals(-1, compare("2.2.0.redhat-1", "2.2.0-redhat-00001"));
    }

    @Test
    public void testRedhatSP() {

        assertEquals(1, compare("2.2.0.SP1-redhat-1", "2.2.0-redhat-3"));
    }

    @Test
    public void sth() {
        assertEquals(1, compare("2.7.7.redhat-7", "2.7.7-redhat-1"));
    }

    @Test
    public void anotherVersion()
    {
        assertEquals(1, compare("1.3.1.SP1-redhat-1", "1.3.1-redhat-3"));
//        assertEquals(1, compare("1.3.16.SP1-redhat-6", "1.3.16-redhat-1"));
    }

    private int compare(String version1, String version2) {
        DefaultArtifactVersion v1 = new DefaultArtifactVersion(version1);
        DefaultArtifactVersion v2 = new DefaultArtifactVersion(version2);
        return VersionComparators.getVersionComparator("mercury").compare(v1, v2);
    }
}
