package in.co.idbibank.etreasury.idbiforex.service;

import in.co.idbibank.etreasury.core.model.TreasuryUserDetails;
import in.co.idbibank.etreasury.idbiforex.exception.IdbiForexDataException;
import in.co.idbibank.etreasury.idbiforex.model.IdbiForexDeal;
import in.co.idbibank.etreasury.idbiforex.repository.IdbiForexDealRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class IdbiForexDealService {

    static final String REPORTED_DEAL_ACTION = "REPO";

    private final IdbiForexDealRepository repository;

    public IdbiForexDealService(IdbiForexDealRepository repository) {
        this.repository = repository;
    }

    /**
     * Validates session user data, formats the date expected by the legacy package and removes null rows.
     */
    public List<IdbiForexDeal> loadDeals(TreasuryUserDetails user, LocalDate transactionDate) {
        if (user == null || user.userId() <= 0) {
            throw new IdbiForexDataException("Authenticated user details are unavailable");
        }
        if (!StringUtils.hasText(user.userCode())) {
            throw new IdbiForexDataException("User code is unavailable for the authenticated user");
        }
        if (!StringUtils.hasText(user.solId())) {
            throw new IdbiForexDataException("SOL ID is unavailable for the authenticated user");
        }

        LocalDate effectiveDate = transactionDate == null ? LocalDate.now() : transactionDate;

        return repository.findDeals(
                        user.userCode(),
                        REPORTED_DEAL_ACTION,
                        effectiveDate,
                        user.solId())
                .stream()
                .filter(Objects::nonNull)
                .toList();
    }
}
