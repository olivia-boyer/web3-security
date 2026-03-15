// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.26;

// Import this file to use console.log
import "hardhat/console.sol";

interface NFT {
    function mintNFT() external;
    function enterAddressIntoBook(string memory) external;

    function transferFrom(address, address, uint) external;
}

contract Auction {
    uint public startTime;
    uint public endTime;
    address payable public owner;

    address payable public highestBidder;
    uint public highestBid;

    NFT nft;
    uint nftId;

    mapping(address => uint256) public fundsPerBidder;

    event Withdrawal(uint amount, uint when);

    constructor(address _nft, uint _id) {
        nft = NFT(_nft);
        nftId = _id;
         
        owner = payable(msg.sender);
    }

    modifier onlyOwner() {
        require(msg.sender == owner, "sender is not owner");
        _;
    }

    modifier isActive() {
        require(block.timestamp > startTime && startTime > 0 && endTime == 0, "Auction not yet active");
        _;
    }

    modifier isClosed() {
        require(block.timestamp > endTime && endTime > 0, "Can't close the auction until its open");
        _;
    }

    error BidTooLow();
    error SenderAlreadyBid();
    error SenderHasNotYetBid();
    error CantRefundWinner();

    function startAuction() public onlyOwner() {
        /* 
            Start the auction by setting the startTime variable
            Permissions - only the owner should be allowed to start the auction.
         */
        startTime = block.timestamp;
        endTime = 0;

    }

    function endAuction() public onlyOwner() isActive() {
        /* 
            End the auction by setting the startTime variable
            Permissions - only the owner should be allowed to end the auction.
         */
        endTime = block.timestamp;
    }

    function makeBid() public payable isActive() {
        /* 
            Only allow the bid to go through if it is higher than the current highest bid and the bidder has not yet bid.
            Set the highestBidder, and highestBid variables accordingly.
            
            Update the fundsPerBidder map.
         */
        require(msg.value > highestBid, "bid too low");
        require (fundsPerBidder[msg.sender] == 0, "sender already bid");
        fundsPerBidder[msg.sender] = msg.value;
        highestBidder = payable(msg.sender);
        highestBid = msg.value;

    }

    function upBid() public payable isActive() {
        /* 
            upBid will update the bidder's bid to their current bid + the msg.value being added.
            Only allow the upBid to go through if their new bid price is higher than the current bid and they have already bid. 

            Set the highestBidder, and highestBid variables accordingly.
            
            Update the fundsPerBidder map.

        */
           if (fundsPerBidder[msg.sender] != 0) {
            if (fundsPerBidder[msg.sender] + msg.value > highestBid) {
            fundsPerBidder[msg.sender] += msg.value;
            highestBidder = payable(msg.sender);
            highestBid = fundsPerBidder[msg.sender];
            } else {
                revert BidTooLow();
            }
        } else {
            revert SenderHasNotYetBid();
        }

    }

    function refund() public isClosed() {
        /* 
            For the refunds, the loser will individually call this function.
            Refunds won't be made to all losers in a batch. You will see in Part 3 why that is a bad design pattern.
            Design this function such that only the msg.sender is refunded. 
        
            Bidders can refund themselves only when the auction is closed.
            Only allow the auction losers to be refunded.

            Update the fundsPerBidder mapping and transfer the refund to the bidder.
            
            Hint 1: You only need a reciever's public key to send them ETH. 
            Hint 2: Use the solidity transfer function to send the funds. 
        */
       if (msg.sender != highestBidder) {
        //copied transfer method from here: https://www.cyfrin.io/glossary/sending-ether-transfer-send-call-solidity-code-example
        bool bidded = fundsPerBidder[msg.sender] > 0;
        require(bidded, "Sender didn't bid!");
        (bool sent, ) = payable(msg.sender).call{value: fundsPerBidder[msg.sender]}("");
        require(sent, "Failed to send Ether");
        fundsPerBidder[msg.sender] = 0;
       } else {
        revert CantRefundWinner();
       }

    }

    function payoutWinner() public onlyOwner() isClosed() {
        fundsPerBidder[highestBidder] = 0;
        nft.enterAddressIntoBook("auction");
        nft.mintNFT();
        nft.transferFrom(address(this), highestBidder, 2);
    }
}

