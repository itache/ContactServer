package me.itache.util;

import me.itache.filter.Cursor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

public class LinkBuilderTest {
    private static LinkBuilder linkBuilder;

    @BeforeClass
    public static void init() {
        linkBuilder = new LinkBuilder(
                ServletUriComponentsBuilder.fromRequestUri(getMockHttpServletRequest()));
    }

    @Test
    public void shouldBuildSelfLinkOnlyForward() {
        Cursor cursor = new Cursor(0, Cursor.Direction.FORWARD, Optional.of(100));
        cursor.setUpperBound(0);
        Map<String, String> links = linkBuilder.build(cursor, new QueryParameter[]{});
        Assert.assertTrue(links.size() == 1);
        Assert.assertEquals(
                "http://localhost:8080/contacts?size=100",
                links.get("self"));
    }

    @Test
    public void shouldBuildSelfLinkOnlyBackward() {
        Cursor cursor = new Cursor(10, Cursor.Direction.BACKWARD, Optional.of(100));
        cursor.setMaxId(9);
        QueryParameter[] parameters = {
                new QueryParameter("nameFilter", "A.*")};
        Map<String, String> links = linkBuilder.build(cursor, parameters);
        Assert.assertTrue(links.size() == 1);
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=A.*&before=10&size=100",
                links.get("self"));
    }

    @Test
    public void shouldBuildSelfAndNextLinksWithAddParamsForward() {
        Cursor cursor = new Cursor(0, Cursor.Direction.FORWARD, Optional.of(100));
        cursor.setUpperBound(10);
        QueryParameter[] parameters = {
                new QueryParameter("nameFilter", "^A.*$")};
        Map<String, String> links = linkBuilder.build(cursor, parameters);
        Assert.assertTrue(links.size() == 2);
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&size=100",
                links.get("self"));
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&after=10&size=100",
                links.get("next"));
    }

    @Test
    public void shouldBuildSelfAndNextLinksWithAddParamsBacward() {
        Cursor cursor = new Cursor(10, Cursor.Direction.BACKWARD, Optional.of(100));
        cursor.setUpperBound(0);
        cursor.setMaxId(29);
        QueryParameter[] parameters = {
                new QueryParameter("nameFilter", "^A.*$")};
        Map<String, String> links = linkBuilder.build(cursor, parameters);
        Assert.assertTrue(links.size() == 2);
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&before=10&size=100",
                links.get("self"));
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&after=9&size=100",
                links.get("next"));
    }

    @Test
    public void shouldBuildSelfAndPreviousLinksForward() {
        Cursor cursor = new Cursor(100, Cursor.Direction.FORWARD, Optional.of(100));
        cursor.setUpperBound(0);
        QueryParameter[] parameters = {
                new QueryParameter("nameFilter", "^A.*$")};
        Map<String, String> links = linkBuilder.build(cursor, parameters);
        Assert.assertTrue(links.size() == 2);
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&after=100&size=100",
                links.get("self"));
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&before=101&size=100",
                links.get("previous"));
    }

    @Test
    public void shouldBuildSelfAndPreviousLinksBackward() {
        Cursor cursor = new Cursor(100, Cursor.Direction.BACKWARD, Optional.of(10));
        cursor.setUpperBound(83);
        cursor.setMaxId(99);
        QueryParameter[] parameters = {
                new QueryParameter("nameFilter", "^A.*$")};
        Map<String, String> links = linkBuilder.build(cursor, parameters);
        Assert.assertTrue(links.size() == 2);
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&before=100&size=10",
                links.get("self"));
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&before=83&size=10",
                links.get("previous"));
    }

    @Test
    public void shouldAllLinksForward() {
        Cursor cursor = new Cursor(1000, Cursor.Direction.FORWARD, Optional.of(100));
        cursor.setUpperBound(1110);
        cursor.setMaxId(2000);
        QueryParameter[] parameters = {
                new QueryParameter("nameFilter", "^A.*$")};
        Map<String, String> links = linkBuilder.build(cursor, parameters);
        Assert.assertTrue(links.size() == 3);
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&after=1000&size=100",
                links.get("self"));
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&before=1001&size=100",
                links.get("previous"));
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&after=1110&size=100",
                links.get("next"));
    }

    @Test
    public void shouldAllLinksBackward() {
        Cursor cursor = new Cursor(1000, Cursor.Direction.BACKWARD, Optional.of(10));
        cursor.setUpperBound(880);
        cursor.setMaxId(2000);
        QueryParameter[] parameters = {
                new QueryParameter("nameFilter", "^A.*$")};
        Map<String, String> links = linkBuilder.build(cursor, parameters);
        Assert.assertTrue(links.size() == 3);
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&before=1000&size=10",
                links.get("self"));
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&before=880&size=10",
                links.get("previous"));
        Assert.assertEquals(
                "http://localhost:8080/contacts?nameFilter=^A.*$&after=999&size=10",
                links.get("next"));
    }


    private static MockHttpServletRequest getMockHttpServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("http");
        request.setServerName("localhost");
        request.setServerPort(8080);
        request.setRequestURI("/contacts");
        return request;
    }
}
