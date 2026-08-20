package in.co.idbibank.etreasury.idbiforex.model;

import java.math.BigDecimal;

/**
 * Immutable view of one IDBIForex deal returned by IFXPRO_DEALING.DEALS_FOR_SAME_SOL_SP.
 */
public record IdbiForexDeal(
        String dealNumber,
        String applicationSystemDate,
        String dealType,
        String customerId,
        String customerName,
        String cardRates,
        String branchCode,
        String transactionType,
        String currencyOne,
        String currencyTwo,
        BigDecimal amount,
        String valueDateOne,
        String valueDateTwo,
        String rate,
        String type,
        String nostroAccount,
        String originalRate,
        String preferredCustomer,
        String actualHighFloor,
        String actualLowFloor,
        String customerFloor,
        String profitLoss,
        String remarks,
        String spotRate,
        String forwardPremia,
        String margin,
        String netRate,
        String bookedBy,
        String bookingDate,
        String confirmedBy,
        String confirmationDate,
        String status,
        String tradeDate,
        String spotDate,
        String maturityDate,
        String solId,
        String optionDate,
        String treasuryDealer,
        String treasuryTransferDate,
        String transmitted,
        String marginId,
        String dealRequestId,
        String swapCharges,
        String forwardContractNumber,
        String dummy,
        String userSol,
        String vertCode,
        String udr,
        String fund,
        String ucType) {

    public String currencyPair() {
        if (currencyOne == null || currencyOne.isBlank()) {
            return currencyTwo == null ? "" : currencyTwo;
        }
        if (currencyTwo == null || currencyTwo.isBlank()) {
            return currencyOne;
        }
        return currencyOne + "/" + currencyTwo;
    }
}
