package mate.academy.model.dto;

import java.util.List;

public class ShoppingCartDto {
    private List<TicketDto> ticketsDto;
    private Long userId;

    public ShoppingCartDto() {
    }

    public List<TicketDto> getTicketsDto() {
        return ticketsDto;
    }

    public void setTicketsDto(List<TicketDto> ticketsDto) {
        this.ticketsDto = ticketsDto;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
