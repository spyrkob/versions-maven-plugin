package org.codehaus.mojo.versions;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.mojo.versions.api.ArtifactVersions;
import org.codehaus.mojo.versions.api.DefaultVersionsHelper;
import org.codehaus.mojo.versions.api.VersionsHelper;
import org.codehaus.mojo.versions.utils.DependencyComparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Mojo(name="export-manifest", aggregator = true, requiresDependencyResolution=ResolutionScope.RUNTIME)
public class ExportManifestMojo extends AbstractVersionsReport {

    @Parameter( property = "reactorProjects", readonly = true )
    private List<MavenProject> reactorProjects;

    @Parameter( property = "manifestFile" )
    private String manifestFile;


    protected Set<Dependency> getDependencies() {
        TreeSet<Dependency> set = new TreeSet<>(new DependencyComparator());

        for (MavenProject child : reactorProjects) {

            for (Object dep : child.getDependencies()) {
                set.add((Dependency)dep);
            }
        }

        return set;
    }

    @Override
    public void execute() throws MojoExecutionException {
        Set<Dependency> dependencies = getDependencies();

        try (PrintWriter pw = new PrintWriter( manifestFile, "UTF8" )) {
            Map<Dependency, ArtifactVersions> dependencyUpdates =
                    getHelper().lookupDependenciesUpdates( dependencies, false );

            for (Dependency dependency : dependencyUpdates.keySet()) {
                ArtifactVersions versions = dependencyUpdates.get(dependency);

                String line = dependency.getGroupId() + ":" + dependency.getArtifactId() + ":" + StringUtils.join(Arrays.asList(versions.getVersions(false)), ";");
                pw.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ArtifactMetadataRetrievalException e) {
            e.printStackTrace();
        } catch (InvalidVersionSpecificationException e) {
            e.printStackTrace();
        } catch (MavenReportException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGenerateReport(Locale locale, Sink sink) throws MavenReportException, MojoExecutionException {

    }

    @Override
    public String getOutputName() {
        return null;
    }
}
