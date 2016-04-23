class Array
  def average
    total = 0.0
    self.each do |item|
      total += item
    end
    total / self.size
  end
end

class MainController < ApplicationController
  def index
    @heartrates = Vital.where(device: "Moto", sensor: "heartrate").where("value <> 0").order("created_at asc").where("created_at > ?", 5.minutes.ago)
    @temperatures = Bath.where(sensor: "temperature").where("value <> 0").order("created_at asc").where("created_at > ?", 5.minutes.ago)
  end

  def vital
    prev_id = params[:prev_id]
    @heartrates = Vital.where(device: "Moto", sensor: "heartrate").where("id > '#{prev_id}'").where("value <> 0").order("created_at asc").where("created_at > ?", 5.minutes.ago)
    render :json => @heartrates, :methods => :formatted_date
  end

  def bath
    prev_id = params[:prev_id]
    @temperatures = Bath.where(sensor: "temperature").where("id > '#{prev_id}'").where("value <> 0").order("created_at asc").where("created_at > ?", 5.minutes.ago)
    render :json => @temperatures, :methods => :formatted_date
  end

  def alert
    @heartrates = Vital.where(device: "Moto", sensor: "heartrate").where("created_at > ?", 120.minutes.ago)
    render :json => (@heartrates.size > 10 && @heartrates.map(&:value).average > 80)
  end
end
