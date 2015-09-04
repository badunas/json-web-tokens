package org.badun.jwtdemo.service.security.token;

import java.util.List;
import java.util.Optional;

/**
 * Created by Artsiom Badun.
 */
public class Claims {
    private final List<Claim> claimList;

    public Claims(List<Claim> claims) {
        this.claimList = claims;
    }

    public List<Claim> getClaims() {
        return claimList;
    }

    public List<Claim> getClaimsExcept(ClaimName claimName) {
        return claimList;
    }

    public String getValue(ClaimName claimName) {
        Optional<Claim> first = claimList.stream()
                .filter(claim -> claim.name() == claimName)
                .findFirst();
        if (first.isPresent()) {
            return first.get().getValue();
        }
        throw new ClaimException("Claim is not presented: " + claimName.val());
    }

    public Claim get(int index) {
        return claimList.get(index);
    }
}
