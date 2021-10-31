package mate.academy.service.mapper;

import java.util.List;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.model.dto.CinemaHallDto;
import mate.academy.model.dto.MovieDto;
import mate.academy.model.dto.MovieSessionDto;
import mate.academy.model.dto.ShoppingCartDto;
import mate.academy.model.dto.TicketDto;
import mate.academy.model.dto.UserDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SupportMapper {

    MovieDto movieEntityToDto(Movie movie);

    CinemaHallDto cinemaHallEntityToDto(CinemaHall cinemaHall);

    UserDto userEntityToDto(User user);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "movieSessionId", source = "movieSession.id")
    TicketDto ticketEntityToDto(Ticket ticket);

    @Mapping(target = "userId", source = "user.id")
    ShoppingCartDto shoppingCartEntityToDto(ShoppingCart shoppingCart);

    @Mapping(target = "movieTitle", source = "movie.title")
    @Mapping(target = "cinemaHallDescription", source = "cinemaHall.description")
    MovieSessionDto movieSessionEntityToDto(MovieSession movieSession);

    List<TicketDto> ticketEntitiesToListDto(List<Ticket> tickets);
}
