package com.example.ecommerce.entity;

import com.example.ecommerce.dto.PostInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Table(name = "post")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "postInfoDto",
                        classes = @ConstructorResult(
                                targetClass = PostInfoDto.class,
                                columns = {
                                        @ColumnResult(name = "id", type = Long.class),
                                        @ColumnResult(name = "slug", type = String.class),
                                        @ColumnResult(name = "title", type = String.class),
                                        @ColumnResult(name = "created_time", type = String.class),
                                        @ColumnResult(name = "published_time", type = String.class),
                                        @ColumnResult(name = "status", type = String.class)
                                }
                        )
                )
        }
)
@NamedNativeQuery(
        name = "adminGetListPost",
        resultSetMapping = "postInfoDto",
        query = "SELECT id, slug, title, \n" +
                "DATE_FORMAT(created_at,'%d/%m/%Y %H:%i') as created_time,\n" +
                "DATE_FORMAT(published_at,'%d/%m/%Y %H:%i') as published_time,\n" +
                "(\n" +
                "\tCASE \n" +
                "\t\tWHEN status = true THEN 'Công khai'\n" +
                "    \tELSE 'Nháp'\n" +
                "    END \n" +
                ") as status\n" +
                "FROM post\n" +
                "WHERE title LIKE CONCAT('%',:title,'%') AND status LIKE CONCAT('%',:status,'%')\n" +
                "ORDER BY :order :direction\n" +
                "LIMIT :limit\n" +
                "OFFSET :offset"
)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 300)
    private String title;

    @Column(name = "slug", nullable = false, length = 600)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "status", columnDefinition = "int default 0")
    private int status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Column(name = "published_at")
    private Timestamp publishedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.EAGER)
    private List<CommentPost> commentPosts = new ArrayList<>();

}