package org.codehaus.mojo.versions;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.versions.api.PomHelper;
import org.codehaus.mojo.versions.utils.DependencyComparator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Mojo(aggregator = true, name="aggregate-dependency-updates-report")
public class AggregateDependencyUpdatesReport extends DependencyUpdatesReport {

    @Parameter( property = "reactorProjects", readonly = true )
    private List<MavenProject> reactorProjects;

    @Override
    protected Set<Dependency> getDependencies() {
        TreeSet<Dependency> set = new TreeSet<>(new DependencyComparator());

        for (MavenProject child : reactorProjects) {

            for (Object dep : child.getDependencies()) {
                set.add((Dependency)dep);
            }
        }

        return set;
    }
}
