package io.nology.supportdesk.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


public class CommentServiceTest {

}

//Test	Expected behaviour
//addComment — valid content	Saves the comment against the ticket and author
//getCommentsByTicketId	Returns comments for the requested ticket
//addComment — blank content	Rejects the comment
//addComment — ticket doesn't exist	Rejects the request
//addComment — user isn't authorised	Rejects the request, if permissions are enforced here
//deleteComment — permitted deletion	Deletes the comment, if deletion is supported
//deleteComment — unauthorised deletion	Rejects the request, if deletion is restricted