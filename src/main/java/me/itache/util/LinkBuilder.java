package me.itache.util;

import me.itache.filter.Cursor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Builds links to adjacent sub-lists based on cursor information and query parameters.
 */
public class LinkBuilder {
    private UriComponentsBuilder uriComponentsBuilder;

    public LinkBuilder(UriComponentsBuilder uriComponentsBuilder) {
        this.uriComponentsBuilder = uriComponentsBuilder;
    }

    /**
     *
     * @param cursor
     * @param parameters
     * @return map in format 'rel' => 'link'(
     * eg. 'next' => 'http://localhost/contacts?nameFilter=^A.*$&after=1245&size=100')
     */
    public Map<String, String> build(Cursor cursor, QueryParameter[] parameters) {
        Map<String, String> links = new HashMap<>();
        if (hasNextPage(cursor)) {
            links.put(Link.NEXT.toString(), createLink(Link.NEXT, cursor, parameters));
        }
        links.put(Link.SELF.toString(), createLink(Link.SELF, cursor, parameters));
        if (hasPreviousPage(cursor)) {
            links.put(Link.PREVIOUS.toString(), createLink(Link.PREVIOUS, cursor, parameters));
        }
        return links;
    }

    private String createLink(Link link, Cursor cursor, QueryParameter[] parameters) {
        UriComponentsBuilder builder = uriComponentsBuilder.cloneBuilder();
        Arrays.stream(parameters).forEach(p -> builder.queryParam(p.getName(), p.getValue()));
        return builder
                .query(link.toQuery(cursor))
                .queryParam("size", cursor.getSize())
                .build()
                .toUriString();
    }

    private boolean hasPreviousPage(Cursor cursor) {
        if(cursor.getDirection() == Cursor.Direction.FORWARD) {
            return cursor.getLowerBound() > 0;
        }
        return cursor.getUpperBound() > 0;
    }

    private boolean hasNextPage(Cursor cursor) {
        if(cursor.getDirection() == Cursor.Direction.FORWARD) {
            return cursor.getUpperBound() > 0;
        }
        return cursor.getLowerBound() <= cursor.getMaxId();
    }

    private enum Link {
        NEXT {
            @Override
            public String toQuery(Cursor cursor) {
                if(cursor.getDirection() == Cursor.Direction.BACKWARD) {
                    return "after=" + (cursor.getLowerBound() - 1);
                }
                return "after=" + cursor.getUpperBound();
            }
        },
        SELF {
            @Override
            public String toQuery(Cursor cursor) {
                if(cursor.getLowerBound() > 0 && cursor.getDirection() == Cursor.Direction.FORWARD) {
                    return "after=" + cursor.getLowerBound();
                }
                if(cursor.getDirection() == Cursor.Direction.BACKWARD) {
                    return "before=" + cursor.getLowerBound();
                }
                return "";
            }
        },
        PREVIOUS {
            @Override
            public String toQuery(Cursor cursor) {
                if(cursor.getDirection() == Cursor.Direction.FORWARD) {
                    return "before=" + (cursor.getLowerBound() + 1);
                }
                return "before=" + cursor.getUpperBound();
            }
        };

        public abstract String toQuery(Cursor cursor);

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
